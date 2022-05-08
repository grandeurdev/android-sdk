package dev.grandeur.android.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Encoder {

    private static final String CHARSET = StandardCharsets.UTF_8.name();

    public static String encodeURIComponent(String text) {
        if (text.isEmpty()) {
            return "";
        }
        String code;
        try {
            code = URLEncoder.encode(text, CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        int textLength = text.length();
        int codeLength = code.length();
        StringBuilder builder = new StringBuilder((textLength + codeLength + 1) / 2);
        for (int i = 0; i < codeLength; ++i) {
            char entry = code.charAt(i);
            switch (entry) {
                case '+':
                    builder.append("%20");
                    break;
                case '%':
                    if (i > codeLength - 2) {
                        break;
                    }
                    char a = code.charAt(i += 1);
                    char b = code.charAt(i += 1);
                    switch (a) {
                        case '2':
                            switch (b) {
                                case '1':
                                    builder.append("!");
                                    break;
                                case '7':
                                    builder.append("'");
                                    break;
                                case '8':
                                    builder.append("(");
                                    break;
                                case '9':
                                    builder.append(")");
                                    break;
                                default:
                                    builder.append("%");
                                    builder.append(a);
                                    builder.append(b);
                                    break;
                            }
                            break;
                        case '7':
                            if (b == 'e' || b == 'E') {
                                builder.append("~");
                            } else {
                                builder.append("%");
                                builder.append(a);
                                builder.append(b);
                            }
                            break;
                        default:
                            builder.append("%");
                            builder.append(a);
                            builder.append(b);
                            break;
                    }
                    break;
                default:
                    builder.append(entry);
                    break;
            }
        }
        return builder.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}