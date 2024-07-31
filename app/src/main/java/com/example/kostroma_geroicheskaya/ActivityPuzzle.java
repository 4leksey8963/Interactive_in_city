package com.example.kostroma_geroicheskaya;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivityPuzzle extends BaseActivity {
    ImageButton blank = null;
    ImageButton[] b = new ImageButton[9];
    boolean isFirstClick = true;

    Drawable temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        b[0] = (ImageButton) findViewById(R.id.button0);
        b[1] = (ImageButton) findViewById(R.id.button1);
        b[2] = (ImageButton) findViewById(R.id.button2);
        b[3] = (ImageButton) findViewById(R.id.button3);
        b[4] = (ImageButton) findViewById(R.id.button4);
        b[5] = (ImageButton) findViewById(R.id.button5);
        b[6] = (ImageButton) findViewById(R.id.button6);
        b[7] = (ImageButton) findViewById(R.id.button7);
        b[8] = (ImageButton) findViewById(R.id.button8);

        blank = (ImageButton) findViewById(R.id.tempbtn);
        //blank = b[8];

        for(int i = 0; i < 9; i++){
            b[i].setTag(i);
            b[i].setOnClickListener(v -> {
                if (isFirstClick) {
                    blank = (ImageButton) v;
                    isFirstClick = false;
                    blank.setImageDrawable(((ImageButton)v).getDrawable());
                    v.setClickable(false);
                } else {
                    Drawable picture1 = blank.getBackground();
                    Drawable picture2 = ((ImageButton)v).getBackground();
                    ImageButton last = blank;
                    blank.setImageDrawable(picture2);
                    last.setImageDrawable(picture1);
                    blank.setClickable(true);
                    isFirstClick = true;
                }
            });
        }

        b[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable temp = b[1].getDrawable();
                b[1].setImageDrawable(b[2].getDrawable());
                b[2].setImageDrawable(temp);
            }
        });
    }
}