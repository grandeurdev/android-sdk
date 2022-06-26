package dev.grandeur.android.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class LocalStorage {
    private Context _context;
    public LocalStorage(Context context) { _context = context; }

    public String getItem(String location) throws IOException {
        File file = new File(_context.getFilesDir(), location);
        try {
            FileInputStream fis = new FileInputStream (file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            String data = new BufferedReader(inputStreamReader).readLine();
            fis.close();
            return data;
        } catch (IOException e) {
            file.createNewFile();
            return null;
        }
    }

    public void setItem(String location, String data) throws IOException {
        File file = new File(_context.getFilesDir(), location);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data.toString().getBytes());
        fos.close();
    }
}
