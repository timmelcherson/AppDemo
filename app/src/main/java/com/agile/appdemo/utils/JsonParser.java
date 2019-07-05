package com.agile.appdemo.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonParser {

    public String loadJSONFromAsset(Context context, String filePath){

        String json;

        try {
            InputStream stream = context.getAssets().open(filePath);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
