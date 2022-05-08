package dev.grandeur.android.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LocalStorage {
    private Context _context;
    public LocalStorage(Context context) {
        _context = context;
    }

    public String getItem(String location) throws IOException {
        FileInputStream fis = _context.openFileInput(location);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        String data = new BufferedReader(inputStreamReader).readLine();
        fis.close();

        return data;
    }

    public void setItem(String location, String data) throws IOException {
        FileOutputStream fos = _context.openFileOutput(location, Context.MODE_PRIVATE);
        fos.write(data.toString().getBytes());
        fos.close();
    }
}
