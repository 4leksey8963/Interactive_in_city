package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class GamesHubActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_hub);
        Button next = findViewById(R.id.next);
        next.setVisibility(View.INVISIBLE);
        setStatusBarColor(R.color.darkGreen);
        Button back = findViewById(R.id.Back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(GamesHubActivity.this, ModeMenuActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GamesHubActivity.this, ModeMenuActivity.class);
        startActivity(intent);
    }
}