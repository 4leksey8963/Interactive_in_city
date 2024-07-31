package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.ranges.IntRange;

public class TestActivity extends BaseActivity {

    Button bt1, bt2, bt3, bt4, continueButton;
    Timer restartTimer;
    TestControl[] masControl = new TestControl[3];
    TestControl control1 = new TestControl();
    TestControl control2 = new TestControl();
    TestControl control3 = new TestControl();
    ProgressBar bar;
    TextView textBar, question;
    LinearLayout hideLayout;
    int count = 0;

    int countQuestion = 0;
    String serialisetext;
    TextView setProgress;

    private void RandomPosition() {
        TestControl[] controls = { control1, control2, control3 };
        Random random = new Random();
        for (int i = 0; i < masControl.length; i++) {
            int randomIndex = random.nextInt(controls.length);
            masControl[i] = controls[randomIndex];
            controls[randomIndex] = controls[controls.length - 1];
            TestControl[] newControls = new TestControl[controls.length - 1];
            System.arraycopy(controls, 0, newControls, 0, controls.length - 1);
            controls = newControls;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RandomPosition();
        setStatusBarColor(R.color.darkGreen);
        Button back = findViewById(R.id.Back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(TestActivity.this, StationMenuActivity.class);
            startActivity(intent);
        });
        Button next = findViewById(R.id.next);
        next.setOnClickListener(v -> {
            Intent intent = new Intent(TestActivity.this, TecnologyActivity.class);
            startActivity(intent);
        });
        setProgress = findViewById(R.id.TextProgress);
        bar = findViewById(R.id.ProgressBar);
        bar.setMax(100);
        textBar = findViewById(R.id.TextProgress);
        hideLayout = findViewById(R.id.HideLayer);
        bt1 = findViewById(R.id.otv1);
        bt2 = findViewById(R.id.otv2);
        bt3 = findViewById(R.id.otv3);
        bt4 = findViewById(R.id.otv4);
        continueButton = findViewById(R.id.Continue);
        continueButton.setVisibility(View.INVISIBLE);

        continueButton.setOnClickListener(v -> {
            if (!checkSerializedKeyExistence("station", "test"+serialisetext, TestActivity.this)) {
                serializeText("station", "station", "test"+serialisetext);
                Integer temp = Integer.parseInt(deserializeText("progress_rewards", "progress_rewards", TestActivity.this))+5;
                serializeText(String.valueOf(temp), "progress_rewards", "progress_rewards");
            }
            Intent intent = new Intent(TestActivity.this, StationMenuActivity.class);
            startActivity(intent);
        });



        bt1.setOnClickListener(v -> changeButtonColor(masControl[countQuestion].getOtv1(), transformColor("#00531D"), transformColor("#5F0000"), (Button) v));


        bt2.setOnClickListener(v -> changeButtonColor(masControl[countQuestion].getOtv2(), transformColor("#00531D"), transformColor("#5F0000"), (Button) v));


        bt3.setOnClickListener(v -> changeButtonColor(masControl[countQuestion].getOtv3(), transformColor("#00531D"), transformColor("#5F0000"), (Button) v));


        bt4.setOnClickListener(v -> changeButtonColor(masControl[countQuestion].getOtv4(), transformColor("#00531D"), transformColor("#5F0000"), (Button) v));



        question = findViewById(R.id.Question);
        try {
            serialisetext = deserializeText("serIndexStation", "index", TestActivity.this);
            int temp = Integer.parseInt(deserializeText("serIndexStation", "serIndexStation"+serialisetext, TestActivity.this));
            switch (temp) {
                case 0: {

                    control1.addFalseOtv(new String[]{"Местные школьники", "Городские власти", "Областной военкомат"});
                    control1.setTrueOtv("ТОС микрорайона и неравнодушные жители");
                    control1.setQuestion("Кто инициировал создание мемориала «Жителям Пантусово» в Костроме?");

                    control2.addFalseOtv(new String[]{"У дома №3 по первому Пантусовскому проезду", "В центральном парке Костромы", "На месте бывшей школы"});
                    control2.setTrueOtv("У дома №1 по второму Пантусовскому проезду");
                    control2.setQuestion("Где установлен памятный знак?");

                    control3.addFalseOtv(new String[]{"100", "150", "200"});
                    control3.setTrueOtv("125");
                    control3.setQuestion("Сколько солдат ушло на фронт из деревни Пантусово?");

                    bt1.setText(masControl[countQuestion].getStrOtv1());
                    bt2.setText(masControl[countQuestion].getStrOtv2());
                    bt3.setText(masControl[countQuestion].getStrOtv3());
                    bt4.setText(masControl[countQuestion].getStrOtv4());
                    question.setText(masControl[countQuestion].getQuestion());
                    break;
                }
                case 1: {
                    control1.addFalseOtv(new String[]{"В танковом полку", "В морском полку", "В авиационном полку"});
                    control1.setTrueOtv("В Кремлёвском полку специального назначения");
                    control1.setQuestion("В каком полку проходил срочную службу Евгений Ермаков?");

                    control2.addFalseOtv(new String[]{"Герой Советского Союза", "Орден Красной Звезды", "Заслуженный мастер спорта"});
                    control2.setTrueOtv("Орден Красного Знамени");
                    control2.setQuestion(" Какое звание было присвоено Ермакову посмертно в 1983 году?");

                    control3.addFalseOtv(new String[]{"1985", "1990", "2000"});
                    control3.setTrueOtv("2013");
                    control3.setQuestion("В каком году Ермаков был посмертно удостоен звания «Почётный гражданин города Костромы»?");

                    bt1.setText(masControl[countQuestion].getStrOtv1());
                    bt2.setText(masControl[countQuestion].getStrOtv2());
                    bt3.setText(masControl[countQuestion].getStrOtv3());
                    bt4.setText(masControl[countQuestion].getStrOtv4());
                    question.setText(masControl[countQuestion].getQuestion());
                    break;
                }
                case 2: {
                    control1.addFalseOtv(new String[]{"Орден Победы", "Орден Мужества", "Орден Славы"});
                    control1.setTrueOtv("Герой Советского Союза");
                    control1.setQuestion("Какое звание было присвоено Алексею Голубкову посмертно?");

                    control2.addFalseOtv(new String[]{"2-я ударная армия", "5-я гвардейская армия", "1-я танковая армия"});
                    control2.setTrueOtv("43-я армия");
                    control2.setQuestion("В какой армии служил Голубков?");

                    control3.addFalseOtv(new String[]{"В бою под Москвой", "В бою под Сталинградом", "В бою под Курском"});
                    control3.setTrueOtv("В бою под Швенчёнисом");
                    control3.setQuestion("Где Голубков был смертельно ранен?");

                    bt1.setText(masControl[countQuestion].getStrOtv1());
                    bt2.setText(masControl[countQuestion].getStrOtv2());
                    bt3.setText(masControl[countQuestion].getStrOtv3());
                    bt4.setText(masControl[countQuestion].getStrOtv4());
                    question.setText(masControl[countQuestion].getQuestion());
                    break;
                }
                case 3: {
                    control1.addFalseOtv(new String[]{"Руководитель хора", "Художественный руководитель", "Главный дирижёр"});
                    control1.setTrueOtv("Заместитель начальника ансамбля");
                    control1.setQuestion("Какую изначальную должность занимал Борис Победимский в ансамбле песни и пляски Северного флота?");

                    control2.addFalseOtv(new String[]{"Генерал-майор", "Капитан первого ранга", "Майор"});
                    control2.setTrueOtv("Подполковник");
                    control2.setQuestion("Какое звание имел Борис Победимский?");

                    control3.addFalseOtv(new String[]{"1938", "1943", "1942"});
                    control3.setTrueOtv("1940");
                    control3.setQuestion("В каком году Победимский окончил Костромскую музыкальную школу?");

                    bt1.setText(masControl[countQuestion].getStrOtv1());
                    bt2.setText(masControl[countQuestion].getStrOtv2());
                    bt3.setText(masControl[countQuestion].getStrOtv3());
                    bt4.setText(masControl[countQuestion].getStrOtv4());
                    question.setText(masControl[countQuestion].getQuestion());
                    break;
                }
                case 4: {
                    control1.addFalseOtv(new String[]{"1943", "1945", "1946"});
                    control1.setTrueOtv("1944");
                    control1.setQuestion("В каком году Юрий Беленогов был удостоен звания Героя Советского Союза?");

                    control2.addFalseOtv(new String[]{"Командир батареи", "Командир роты", "Командир взвода"});
                    control2.setTrueOtv(" Командир танка");
                    control2.setQuestion("Какая должность была у Беленогова в армии?");

                    control3.addFalseOtv(new String[]{"В Московском военном училище", "В Костромской офицерской школе", "В Свердловской военной академии"});
                    control3.setTrueOtv("В Пушкинском танковом училище");
                    control3.setQuestion("Где проходила учёба Беленогова перед отправкой на фронт?");

                    bt1.setText(masControl[countQuestion].getStrOtv1());
                    bt2.setText(masControl[countQuestion].getStrOtv2());
                    bt3.setText(masControl[countQuestion].getStrOtv3());
                    bt4.setText(masControl[countQuestion].getStrOtv4());
                    question.setText(masControl[countQuestion].getQuestion());
                    break;
                }
                case 5: {
                    control1.addFalseOtv(new String[]{"В левобережном районе", "В южном районе", "В северном районе"});
                    control1.setTrueOtv("В правобережном районе");
                    control1.setQuestion("В каком районе Костромы установлен памятник танку Т-34-85?");

                    control2.addFalseOtv(new String[]{"Завод «Красный металлист»", "Судомеханический завод", "Авиационный завод"});
                    control2.setTrueOtv("Завод «Рабочий металлист»");
                    control2.setQuestion("Какой завод в Костроме в годы войны выпускал боеприпасы и детали стрелкового оружия?");

                    control3.addFalseOtv(new String[]{"«Костромская дивизия»", "«Защитник Родины»", "«Победитель»"});
                    control3.setTrueOtv("«Иван Сусанин»");
                    control3.setQuestion("Как называлась танковая колонна, построенная на средства костромичей?");

                    bt1.setText(masControl[countQuestion].getStrOtv1());
                    bt2.setText(masControl[countQuestion].getStrOtv2());
                    bt3.setText(masControl[countQuestion].getStrOtv3());
                    bt4.setText(masControl[countQuestion].getStrOtv4());
                    question.setText(masControl[countQuestion].getQuestion());
                    break;
                }
            }
        }
        catch (Exception e) {}

    }

    public void changeButtonColor(boolean condition, int positiveColor, int negativeColor, Button button) {
        if (condition) {
            countQuestion+=1;
            if (countQuestion == masControl.length) {
                button.setBackgroundColor(positiveColor);
                button.setBackgroundTintList(ColorStateList.valueOf(positiveColor));
                button.setForegroundTintList(ColorStateList.valueOf(positiveColor));
                continueButton.setVisibility(View.VISIBLE);
                setEnabledButton(false);
            }
            else {
                setProgress.setText("Готовим новый вопрос");
                button.setBackgroundColor(positiveColor);
                button.setBackgroundTintList(ColorStateList.valueOf(positiveColor));
                button.setForegroundTintList(ColorStateList.valueOf(positiveColor));
                setEnabledButton(false);
                restartTimer = new Timer();
                hideLayout.setVisibility(View.VISIBLE);
                restartTimer.schedule(new TimerTask() {
                    public void run() {
                        ReTimer();
                    }
                }, 0, 10);
            }
        } else {
            setProgress.setText("Загрузка следующей попытки");
            button.setBackgroundColor(negativeColor);
            button.setBackgroundTintList(ColorStateList.valueOf(negativeColor));
            button.setForegroundTintList(ColorStateList.valueOf(negativeColor));
            setEnabledButton(false);
            restartTimer = new Timer();
            hideLayout.setVisibility(View.VISIBLE);
            restartTimer.schedule(new TimerTask() {
                public void run() {
                    ReTimer();
                }
            }, 0, 10);
        }
    }



    private void ReTimer() {
        this.runOnUiThread(doRestart);
    }

    private Runnable doRestart = new Runnable() {
        public void run()
        {
            if (count <= 2000) {
                count += 10;
                if (count%20 == 0)
                    bar.setProgress(bar.getProgress()+1);
                masControl[countQuestion].randomOtv();
            }
            else {
                if (restartTimer != null) {
                    restartTimer.cancel();
                    restartTimer = null;
                    setColorButton();
                    setEnabledButton(true);
                    bt1.setText(masControl[countQuestion].getStrOtv1());
                    bt2.setText(masControl[countQuestion].getStrOtv2());
                    bt3.setText(masControl[countQuestion].getStrOtv3());
                    bt4.setText(masControl[countQuestion].getStrOtv4());
                    question.setText(masControl[countQuestion].getQuestion());
                    count = 0;
                    bar.setProgress(0);
                    hideLayout.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    private void setEnabledButton(boolean status) {
        bt1.setEnabled(status);
        bt2.setEnabled(status);
        bt3.setEnabled(status);
        bt4.setEnabled(status);
    }

    private void setColorButton() {
        bt1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightGreen)));
        bt1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkGreen)));
        bt2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightGreen)));
        bt2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkGreen)));
        bt3.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightGreen)));
        bt3.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkGreen)));
        bt4.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightGreen)));
        bt4.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkGreen)));
    }

    public int transformColor(String colorHex) {
        int color = Color.parseColor(colorHex);
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TestActivity.this, StationMenuActivity.class);
        startActivity(intent);
    }
}