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

public class BeforeStationMeetActivity extends BaseActivity {
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
        setContentView(R.layout.activity_before_station_meet);
        setStatusBarColor(R.color.darkGreen);

        hero = findViewById(R.id.vdv1);
        hero.setImageResource(imageList[currentImageIndex]);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        try {
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
        }
        catch (Exception e) {}


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
                Intent intent = new Intent(BeforeStationMeetActivity.this, StationMenuActivity.class);
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

        try {
            int temp = Integer.parseInt(deserializeText("BeforeHero", "BeforeHero", BeforeStationMeetActivity.this));
            switch (temp) {
                case 0: {
                    control.addToList("Первой станцией нашего путешествия является мемориал жителям Пантусово - участникам Великой Отечественной войны 1941 -1945 годов.");
                    control.addToList("Отправляйтесь туда, чтобы узнать об их бесстрашии и отваге.");
                    break;
                }
                case 1: {
                    control.addToList("Здесь находится памятник в честь Евгения Ермакова, капитана, который не вернулся из боевого задания,");
                    control.addToList("но оставил после себя неизгладимый след в истории.");
                    break;
                }
                case 2: {
                    control.addToList("Алексей Голубков - имя, которое стало символом мужества и самопожертвования.");
                    control.addToList("Он пал смертью храбрых, и сегодня мы можем только склонить головы перед мемориалом в его честь.");
                    control.addToList("Узнав историю Голубкова, Вы проникнитесь уважением к его жизненному пути.");
                    break;
                }
                case 3: {
                    control.addToList("Борис Победимский - человек, чья жизнь была полна творчества и служения Родине.");
                    control.addToList("Он оставил после себя наследие, которое продолжает вдохновлять.");
                    control.addToList("Познакомтесь с его историей и узнайте, как он сочетал искусство с патриотизмом.");
                    break;
                }
                case 4: {
                    control.addToList("Юрий Беленогов - герой, чья отвага и жертва не знали границ. Он погиб в самом расцвете лет, защищая нашу страну.");
                    control.addToList("Почтите его память и узнайте, какие действия делают человека настоящим героем.");
                    break;
                }
                case 5: {
                    control.addToList("И вот вы достигли финальной станции - памятника «Доблестным защитникам отечества, труженикам тыла, победителям в Великой Отечественной войне 1941-1945 г.г.».");
                    control.addToList("Не теряйте ни минуты! Вперёд! Победа близка!");
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
        Intent intent = new Intent(BeforeStationMeetActivity.this, StationMenuActivity.class);
        startActivity(intent);
    }
}
