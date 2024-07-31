package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StationMenuActivity extends BaseActivity {

    String serialisetext;
    Boolean allComplate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_menu);
        setStatusBarColor(R.color.darkGreen);
        CheckBox check1 = findViewById(R.id.check1);
        check1.setEnabled(false);
        CheckBox check2 = findViewById(R.id.check2);
        check2.setEnabled(false);
        CheckBox check4 = findViewById(R.id.check4);
        check4.setEnabled(false);
        TextView test = findViewById(R.id.Test);
        Button back = findViewById(R.id.Back);
        TextView theory = findViewById(R.id.Theory);
        serialisetext = deserializeText("serIndexStation", "index", StationMenuActivity.this);
        try {
            String temp = deserializeText("station", "theory"+serialisetext, StationMenuActivity.this);
            if(temp.length()>0)
                check1.setChecked(true);
        }
        catch (Exception e) {}
        try {
            String temp = deserializeText("station", "test"+serialisetext, StationMenuActivity.this);
            if(temp.length()>0)
                check2.setChecked(true);
        }
        catch (Exception e) {}
        try {
            String temp = deserializeText("station", "checkin"+serialisetext, StationMenuActivity.this);
            if(temp.length()>0)
                check4.setChecked(true);
        }
        catch (Exception e) {}
        try {
            String temp0 = deserializeText("station", "theory"+serialisetext, StationMenuActivity.this);
            String temp1 = deserializeText("station", "test"+serialisetext, StationMenuActivity.this);
            String temp2 = deserializeText("station", "checkin"+serialisetext, StationMenuActivity.this);
            if(temp0.length()>0 && temp1.length()>0 && temp2.length() > 0) {
                allComplate = true;
                Integer a = Integer.parseInt(deserializeText("serComplateStation", "serComplateStation", StationMenuActivity.this));
                if (a < Integer.parseInt(serialisetext))
                    serializeText(serialisetext, "serComplateStation", "serComplateStation");
            }
        }
        catch (Exception e) {}

        if (checkSerializedKeyExistence("station", "theory", StationMenuActivity.this)) {

            deserializeText("progress_rewards", "progress_rewards", StationMenuActivity.this);
        }
        theory.setOnClickListener(v -> {
            Intent intent = new Intent(StationMenuActivity.this, TheoryActivity.class);
            startActivity(intent);
        });

        test.setOnClickListener(v -> {
            Intent intent = new Intent(StationMenuActivity.this, TestActivity.class);
            startActivity(intent);
        });

        TextView check = findViewById(R.id.Check);
        check.setOnClickListener(v -> {
            Intent intent = new Intent(StationMenuActivity.this, TecnologyActivity.class);
            startActivity(intent);
        });

        back.setOnClickListener(v -> {
            int temp = Integer.parseInt(deserializeText("AfterHero", "AfterHero", StationMenuActivity.this));
            if (Integer.parseInt(serialisetext) > temp && allComplate) {
                try {
                serializeText(String.valueOf(serialisetext),"AfterHero", "AfterHero");
                Intent intent = new Intent(this, AfterStationMeetActivity.class);
                startActivity(intent);} catch (Exception e) {}
            }
            else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        Button next = findViewById(R.id.next);
        next.setOnClickListener(v -> {
            Intent intent = new Intent(StationMenuActivity.this, TheoryActivity.class);
            startActivity(intent);
        });


        ImageView backToMap = findViewById(R.id.BackToMap);
        backToMap.setOnClickListener(v -> {

            int temp = Integer.parseInt(deserializeText("AfterHero", "AfterHero", StationMenuActivity.this));
            if (Integer.parseInt(serialisetext) > temp && allComplate) {
                serializeText(String.valueOf(serialisetext),"AfterHero", "AfterHero");
                Intent intent = new Intent(this, AfterStationMeetActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });



        ImageView imageView = findViewById(R.id.StationImage);
        TextView title = findViewById(R.id.Title);
        try {
            int temp = Integer.parseInt(deserializeText("serIndexStation", "serIndexStation"+serialisetext, StationMenuActivity.this));
            switch (temp) {
                case 0: {
                    title.setText("Жителям Пантусово");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.pantusovo));
                    break;
                }
                case 1: {
                    title.setText("Ермаков Е.Л.");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ermakovo));
                    break;
                }
                case 2: {
                    title.setText("Голубков А.К.");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.golubkova));
                    break;
                }
                case 3: {
                    title.setText("Победимский Б.М.");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.pobedisnkiy));
                    break;
                }
                case 4: {
                    title.setText("Беленогов Ю.С.");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.belenogovo));
                    break;
                }
                case 5: {
                    title.setText("Памятник Т-34-85");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.tanka));
                    break;
                }
            }
        }
        catch (Exception e) {}

        try {
            Integer dialogRezult = Integer.parseInt(deserializeText("Dialog", "Dialog", StationMenuActivity.this));
            Integer a = Integer.parseInt(deserializeText("progress_rewards", "progress_rewards", StationMenuActivity.this));
            switch (a) {
                case 25: {
                    if (a > dialogRezult) {
                        serializeText(String.valueOf(a), "Dialog", "Dialog");
                        int bronze = R.drawable.bronze;
                         CustomDialogFragment dialog = new CustomDialogFragment(bronze, "Вы получили\nбронзовую медаль!\nПоздравляем!");
                        dialog.show(getSupportFragmentManager(), "custom");
                    }
                    break;}
                case 50: {
                    if (a > dialogRezult) {
                        serializeText(String.valueOf(a), "Dialog", "Dialog");
                        int silver = R.drawable.silver;
                         CustomDialogFragment dialog = new CustomDialogFragment(silver, "Вы получили\nсеребрянную медаль!\nПоздравляем!");
                        dialog.show(getSupportFragmentManager(), "custom");
                    }
                    break;}
                case 75: {
                    if (a > dialogRezult) {
                        serializeText(String.valueOf(a), "Dialog", "Dialog");
                        int gold = R.drawable.gold;
                        CustomDialogFragment dialog = new CustomDialogFragment(gold, "Вы получили\nзолотую медаль!\nПоздравляем!");
                        dialog.show(getSupportFragmentManager(), "custom");
                    }
                    break;}
                case 100: {
                    if (a > dialogRezult) {
                        serializeText(String.valueOf(a), "Dialog", "Dialog");
                        int platinum = R.drawable.platina;
                        CustomDialogFragment dialog = new CustomDialogFragment(platinum, "Вы получили\nплатиновую медаль!\nПоздравляем!");
                        dialog.show(getSupportFragmentManager(), "custom");
                    }
                    break;}
            }
        }
        catch (Exception e) {}
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StationMenuActivity.this, MainActivity.class);
        startActivity(intent);
    }
}