package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class RewardsActivity extends BaseActivity {

    ProgressBar progressBar;
    TextView textProgress;
    int medal1 = 25, medal2 = 50, medal3 = 75, medal4 = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        setStatusBarColor(R.color.darkGreen);
        Button next = findViewById(R.id.next);
        next.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.ProgressBar);
        progressBar.setMax(100);
        textProgress = findViewById(R.id.TextProgress);
        try {
            String textPercent = deserializeText("progress_rewards", "progress_rewards", RewardsActivity.this);
            if (textPercent.length() > 0) {
                textProgress.setText(textPercent + "%");
                Integer newValue = Integer.valueOf(textPercent);
                progressBar.setProgress(newValue);
            }
        }
        catch (Exception e) {}

        Button back = findViewById(R.id.Back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsActivity.this, ModeMenuActivity.class);
            startActivity(intent);
        });

        Button diplom = findViewById(R.id.Diplom);
        diplom.setOnClickListener(v -> {
            if (progressBar.getProgress() < 100) {
                Toast.makeText(RewardsActivity.this, "Завершите прогресс на 100%", Toast.LENGTH_LONG).show();
            }
            else {
                Intent intent = new Intent(RewardsActivity.this, DiplomActivity.class);
                startActivity(intent);
            }
        });

        ImageView reward1 = findViewById(R.id.Reward1);
        Drawable drawable1 = ContextCompat.getDrawable(RewardsActivity.this, R.drawable.bronze);
        setImageReward(medal1, reward1, drawable1);
        ImageView reward2 = findViewById(R.id.Reward2);
        Drawable drawable2 = ContextCompat.getDrawable(RewardsActivity.this, R.drawable.silver);
        setImageReward(medal2, reward2, drawable2);
        ImageView reward3 = findViewById(R.id.Reward3);
        Drawable drawable3 = ContextCompat.getDrawable(RewardsActivity.this, R.drawable.gold);
        setImageReward(medal3, reward3, drawable3);
        ImageView reward4 = findViewById(R.id.Reward4);
        Drawable drawable4 = ContextCompat.getDrawable(RewardsActivity.this, R.drawable.platina);
        setImageReward(medal4, reward4, drawable4);



        reward1.setOnClickListener(v -> setValueReward(medal1));

        reward2.setOnClickListener(v -> setValueReward(medal2));

        reward3.setOnClickListener(v -> setValueReward(medal3));

        reward4.setOnClickListener(v -> setValueReward(medal4));
    }

    private void setValueReward(Integer value) {
        if (progressBar.getProgress() < value) {
            Toast.makeText(RewardsActivity.this, "Для разблокировки нужно " + String.valueOf(value) + "%", Toast.LENGTH_SHORT).show();
        }
    }

    private void setImageReward(Integer value, ImageView image, Drawable drawable) {
        if (progressBar.getProgress() >= value) {
            image.setForeground(drawable);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RewardsActivity.this, ModeMenuActivity.class);
        startActivity(intent);
    }
}