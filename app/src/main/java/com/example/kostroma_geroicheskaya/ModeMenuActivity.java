package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModeMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_menu);
        setStatusBarColor(R.color.darkGreen);
        Button next = findViewById(R.id.next);
        next.setVisibility(View.INVISIBLE);
        Button map = findViewById(R.id.Map);
        Button games = findViewById(R.id.Games);
        Button rewards = findViewById(R.id.Rewards);
        Button back = findViewById(R.id.Back);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ModeMenuActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });
        rewards.setOnClickListener(v -> {
            Intent intent = new Intent(ModeMenuActivity.this, RewardsActivity.class);
            startActivity(intent);
        });
        games.setOnClickListener(v -> {
            Intent intent = new Intent(ModeMenuActivity.this, GamesHubActivity.class);
            startActivity(intent);
        });

        map.setOnClickListener(v -> {
            Intent intent = new Intent(ModeMenuActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ModeMenuActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }
}