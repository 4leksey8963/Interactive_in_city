package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AfterStationMeetActivity extends BaseActivity {
    TextView dialogWindow;
    Button back, forward;
    ImageView hero;
    Timer HeroTimer;
    int screenWidth, screenHeight;
    LinearLayout controlDialog;
    DialogControl control = new DialogControl();
    Button continueButton;
    int currentImageIndex = 0;
    private int[] imageList = {R.drawable.vdv1, R.drawable.vdv2, R.drawable.vdv3, R.drawable.vdv4, R.drawable.vdv5, R.drawable.vdv6, R.drawable.vdv7, R.drawable.vdv8};
    private void randomImIn() {
        Random random = new Random();
        currentImageIndex = random.nextInt(imageList.length);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_station_meet);

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
                Intent intent = new Intent(AfterStationMeetActivity.this, MainActivity.class);
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
        LinearLayout background = findViewById(R.id.click);
        try {
            int temp = Integer.parseInt(deserializeText("AfterHero", "AfterHero", AfterStationMeetActivity.this));
            switch (temp) {
                case 0: {
                    background.setBackgroundResource(R.drawable.v_pantusovo);
                    control.addToList("Вы показали уважение к истории и солдатам былых времён. Но память о героях здесь не заканчивается...");
                    control.addToList("Следующая станция посвящена Евгению Ермакову, герою, погибшему в Афганистане. Отправляйтесь туда и узнайте о его подвиге.");
                    break;
                }
                case 1: {
                    background.setBackgroundResource(R.drawable.v_ermakov);
                    control.addToList("Вы отдали дань памяти и уважения Ермакову. Теперь направляйтесь к мемориальной доске Алексея Голубкова.");
                    control.addToList("Его подвиги во время освобождения Белоруссии заслуживают того, чтобы их помнили и ценили.");
                    break;
                }
                case 2: {
                    background.setBackgroundResource(R.drawable.v_golubkovo);
                    control.addToList("Вы только что узнали о невероятном мужестве Алексея Голубкова.");
                    control.addToList("Поздравляем, Вы прошли половину тропы героев. Теперь направляйтесь к мемориальной доске Бориса Победимского.");
                    break;
                }
                case 3: {
                    background.setBackgroundResource(R.drawable.v_pobedimskiy);
                    control.addToList("Вы только что познакомился с жизнью и творчеством Бориса Победимского. Это была очень творческая личность!");
                    control.addToList("Далее идёт не менее значимая личность. Пора познакомится с историей Юрия Беленогова, молодого героя, чья жизнь оборвалась на поле боя.");
                    control.addToList("Окружённый врагами он принимает непростое решение - подорвать себя вместе с фашистами.");
                    control.addToList("Его подвиг по сей день напоминает нам о цене мира.");
                    break;
                }
                case 4: {
                    background.setBackgroundResource(R.drawable.v_belenogovo);
                    control.addToList("Мемориальная доска Юрия Беленогова позади. Остался последний рывок!");
                    control.addToList("Ваш следующий вызов - это памятник труженникам тыла, которые в годы войны работали по несколько смен, чтобы обеспечить солдат на поле боя.");
                    control.addToList("Вклад этих людей в победу столь же велик, как и тех, кто был на передовой.");
                    break;
                }
                case 5: {
                    background.setBackgroundResource(R.drawable.v_tank);
                    control.addToList("Поздравляем! Вы прошли все испытания, посвященные героям нашей страны, нашего города, и каждый шаг Вашего пути был наполнен глубоким смыслом.");
                    control.addToList("Вы узнали о героях, которые отстояли нашу Родину. Ваша память и уважение к прошлому - это залог того, что героизм этих людей никогда не будет забыт.");
                    control.addToList("В любое время вы можете перечитать историю и перепройти тесты, чтобы обновить свои знания.");
                    break;
                }
            }
        }
        catch (Exception e) {}
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
        Intent intent = new Intent(AfterStationMeetActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
