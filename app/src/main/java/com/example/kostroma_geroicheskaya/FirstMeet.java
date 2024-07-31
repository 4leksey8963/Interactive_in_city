package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FirstMeet extends BaseActivity {
    TextView dialogWindow;
    Button back, forward;
    ImageView hero;
    Timer HeroTimer;
    int screenWidth, screenHeight;
    LinearLayout controlDialog;
    DialogControl control = new DialogControl();
    Button continueButton;
    private int currentImageIndex = 0;
    private int[] imageList = {R.drawable.vdv1, R.drawable.vdv2, R.drawable.vdv3, R.drawable.vdv4, R.drawable.vdv5, R.drawable.vdv6, R.drawable.vdv7, R.drawable.vdv8};
    private void randomImIn() {
        Random random = new Random();
        currentImageIndex = random.nextInt(imageList.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_meet);
        try {
            String temp = deserializeText("True", "True", FirstMeet.this);
            if (temp.length() > 0) {
                Intent intent = new Intent(FirstMeet.this, MainMenuActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) { }
        setStatusBarColor(R.color.darkGreen);
        hero = findViewById(R.id.vdv1);
        hero.setImageResource(imageList[currentImageIndex]);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        hero.setX(screenWidth*1.25f);
        setSize(hero, 1145, 2000);
        HeroTimer = new Timer();
        HeroTimer.schedule(new TimerTask() {
            public void run() {
                HeroTimer();
            }
        }, 0, 10);

        dialogWindow = findViewById(R.id.TextWindow);
        back = findViewById(R.id.TextBack);
        back.setVisibility(View.INVISIBLE);
        forward = findViewById(R.id.TextForward);
        controlDialog = findViewById(R.id.ControlDialog);
        continueButton = findViewById(R.id.LocationButton);
        continueButton.setVisibility(View.INVISIBLE);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serializeText("True", "True", "True");
                Intent intent = new Intent(FirstMeet.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomImIn();
                hero.setImageResource(imageList[currentImageIndex]);
                control.back();
                changeColor(back, control.getBackEnable());
                changeColor(forward, control.getForwardEnable());
                back.setEnabled(control.getBackEnable());
                forward.setEnabled(control.getForwardEnable());
                dialogWindow.setText(control.getText());
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomImIn();
                hero.setImageResource(imageList[currentImageIndex]);
                control.forward();
                changeColor(back, control.getBackEnable());
                changeColor(forward, control.getForwardEnable());
                back.setEnabled(control.getBackEnable());
                forward.setEnabled(control.getForwardEnable());
                dialogWindow.setText(control.getText());
                if (control.getI() >= control.getDialogSize())
                    continueButton.setVisibility(View.VISIBLE);
            }
        });
        control.addToList("Приглашаем вас в увлекательное путешествие по страницам истории, где каждый шаг открывает героические подвиги наших предков.");
        control.addToList("В этом квесте вы не просто участники - вы становитесь свидетелями величия и отваги, которые оставили след в веках.");
        control.addToList("На каждой станции вас ждут задания, которые не только проверят вашу смекалку и знания, но и позволят глубже понять историю нашей страны.");
        control.addToList("Готовы ли вы к испытанию? Пусть ваше путешествие начнется!");
    }

    private void HeroTimer() {
        this.runOnUiThread(doJump);
    }

    private Runnable doJump = new Runnable() {
        public void run()
        {
            if (hero.getX()>= screenWidth-hero.getWidth()*0.97) {
                hero.setX(hero.getX() - 8);
                hero.setY((float) (screenHeight) /2.0f);
            }
            else   {
                if (HeroTimer != null) {
                    HeroTimer.cancel();
                    HeroTimer = null;
                    back.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);
                    dialogWindow.setVisibility(View.VISIBLE);
//                    controlDialog.setY(hero.getY()+hero.getHeight()+50);
//                    continueButton.setY(controlDialog.getY()+controlDialog.getHeight()+50);
                    dialogWindow.setText(control.getText());
                    back.setEnabled(control.getBackEnable());
                    forward.setEnabled(control.getForwardEnable());
                }
            }
        }
    };

    private void setSize(View view, int x, int y) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = x;
        params.height = y;
        view.setLayoutParams(params);
    }

    private void setTextColor(Button textView, Integer color) {
        String hexColor = String.format("#%06X", ContextCompat.getColor(this, color) & 0xFFFFFF);
        textView.setBackgroundColor(Color.parseColor(hexColor));
    }

    private void changeColor(Button button,boolean b) {
        if (b) {
            setTextColor(button, R.color.darkGreen);
        }
        else {
            setTextColor(button, R.color.disable);
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
