package com.example.kostroma_geroicheskaya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.io.File;

public class CustomWarningFragment extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warning);
        setStatusBarColor(R.color.darkGreen);

        Button btnYes = findViewById(R.id.Yes);
        Button btnNo = findViewById(R.id.No);

        btnNo.setOnClickListener(v -> {
            Intent intent = new Intent(CustomWarningFragment.this, MainMenuActivity.class);
            startActivity(intent);
        });

        btnYes.setOnClickListener(v -> {
            clearSharedPrefsDirectory(CustomWarningFragment.this);
            Intent intent = new Intent(CustomWarningFragment.this, ActivityAuthorization.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CustomWarningFragment.this, MainMenuActivity.class);
        startActivity(intent);
    }
}
