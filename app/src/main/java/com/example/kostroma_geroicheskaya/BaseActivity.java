package com.example.kostroma_geroicheskaya;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;

public class BaseActivity extends AppCompatActivity {

    public void setStatusBarColor(Integer color) {
        String hexColor = String.format("#%06X", ContextCompat.getColor(this, color) & 0xFFFFFF);
        getWindow().setStatusBarColor(Color.parseColor(hexColor));
    }

    protected void serializeText(String text, String filename, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(filename, MODE_PRIVATE).edit();
        editor.putString(key, text);
        editor.apply();
    }

    protected String deserializeText(String filename, String key, Context context) {
        if (checkSerializedFileExistence(filename, context)) {
            SharedPreferences prefs = getSharedPreferences(filename, MODE_PRIVATE);
            if (prefs.contains(key)) {
                return prefs.getString(key, "");
            }
            else return null;
        }
        else return null;
    }

    public boolean checkSerializedFileExistence(String fileName, Context context) {
        File file = new File(context.getFilesDir().getParent() + "/shared_prefs/" + fileName + ".xml");
        return file.exists();
    }

    protected boolean checkSerializedKeyExistence(String filename, String key, Context context) {
        if (checkSerializedFileExistence(filename, context)) {
            SharedPreferences prefs = getSharedPreferences(filename, MODE_PRIVATE);
            if (prefs.contains(key)) {
                return true;
            }
        }
        return false;
    }

    public void clearSharedPrefsDirectory(Context context) {
        File sharedPrefsDir = new File(context.getFilesDir().getParent() + "/shared_prefs/");
        if (sharedPrefsDir.isDirectory()) {
            String[] children = sharedPrefsDir.list();
            for (String child : children) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(child.replace(".xml", ""), Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
            }
        }
    }
}