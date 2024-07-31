package com.example.kostroma_geroicheskaya;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.graphics.ColorUtils;

import java.io.File;

public class MainMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setStatusBarColor(R.color.darkGreen);
        Button next = findViewById(R.id.next);
        next.setVisibility(View.INVISIBLE);
        Button unlock1 = findViewById(R.id.Unlock1);
        Button lock2 = findViewById(R.id.Lock2);
        Button lock3 = findViewById(R.id.Lock3);
        Button exit = findViewById(R.id.Exit);
        Button exitTwo = findViewById(R.id.ExitTwo);

        lock2.setEnabled(false);
        lock3.setEnabled(false);

        Button removeFile = findViewById(R.id.RemoveData);
        removeFile.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, CustomWarningFragment.class);
            startActivity(intent);
        });

        unlock1.setOnClickListener(v -> {
            serializeText(getString(R.string.marsh1), "marsh_name", "marsh_name");
            Intent intent = new Intent(MainMenuActivity.this, ModeMenuActivity.class);
            startActivity(intent);
        });
        exit.setOnClickListener(v -> this.finishAffinity());
        exitTwo.setOnClickListener(v -> this.finishAffinity());

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}