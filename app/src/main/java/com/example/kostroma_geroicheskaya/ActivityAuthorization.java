package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.text.Regex;

public class ActivityAuthorization extends BaseActivity  {

    Timer serialise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        try {
            String deserialise = deserializeText(getString(R.string.User), getString(R.string.User), ActivityAuthorization.this);
            if (deserialise.length() > 0) {
                Intent intent = new Intent(ActivityAuthorization.this, FirstMeet.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {}

        Button next = findViewById(R.id.next);
        next.setVisibility(View.INVISIBLE);
        setStatusBarColor(R.color.darkGreen);
        TextView firstName = findViewById(R.id.FirstName);
        EditText editFirstName = findViewById(R.id.EditFirstName);
        editFirstName.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});
        TextView lastName = findViewById(R.id.LastName);
        EditText editLastName = findViewById(R.id.EditLastName);
        editLastName.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});
        TextView school = findViewById(R.id.School);
        EditText editSchool = findViewById(R.id.EditSchool);
        editSchool.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});
        TextView clas = findViewById(R.id.Class);
        EditText editClass = findViewById(R.id.EditClass);
        editClass.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
        lastRed(firstName);
        lastRed(lastName);
        lastRed(school);
        lastRed(clas);
        setTextColor(firstName, R.color.text);
        setTextColor(lastName, R.color.text);
        setTextColor(school, R.color.text);
        setTextColor(clas, R.color.text);

//        textFiltre(editFirstName, "_", 16);
//        textFiltre(editLastName, "_", 16);
//        textFiltre(editSchool, "_", 16);
//        textFiltre(editClass, "_", 4);

        statusAuthorization(editFirstName, firstName);
        statusAuthorization(editLastName, lastName);
        statusAuthorization(editSchool, school);
        statusAuthorization(editClass, clas);
        Button exitButton = findViewById(R.id.Exit);
        exitButton.setOnClickListener(v -> this.finishAffinity());
        TextView continueButton = findViewById(R.id.LocationButton);
        continueButton.setOnClickListener(v -> {
            try {
                if(editFirstName.length() > 0 && editLastName.length() > 0 && editSchool.length() > 0 && editClass.length() > 0) {

                    serializeText(replaceUnderscoresWithHyphens(editFirstName) + "_" + replaceUnderscoresWithHyphens(editLastName) + "_" +
                            replaceUnderscoresWithHyphens(editSchool) + "_" + replaceUnderscoresWithHyphens(editClass), getString(R.string.User), getString(R.string.User));
                    Intent intent = new Intent(ActivityAuthorization.this, FirstMeet.class);
                    startActivity(intent);}
                else {
                    Toast.makeText(ActivityAuthorization.this, "Заполните все даные перед\n тем, как продолжить", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) { school.setText(e.getMessage());}

        });

        serialise = new Timer();
        serialise.schedule(new TimerTask() {
            public void run() {
                Serialise();
            }
        }, 0, 10);
        if (!checkSerializedKeyExistence("progress_rewards", "progress_rewards", ActivityAuthorization.this)) {
            serializeText("10", "progress_rewards", "progress_rewards");
        }
        if (!checkSerializedKeyExistence("serComplateStation", "serComplateStation", ActivityAuthorization.this)) {
            serializeText("-1", "serComplateStation", "serComplateStation");
        }
        if (!checkSerializedKeyExistence("AfterHero", "AfterHero", ActivityAuthorization.this)) {
            serializeText(String.valueOf(-1),"AfterHero", "AfterHero");
        }
        if (!checkSerializedKeyExistence("BeforeHero", "BeforeHero", ActivityAuthorization.this)) {
            serializeText(String.valueOf(-1),"BeforeHero", "BeforeHero");
        }
        if (!checkSerializedKeyExistence("Dialog", "Dialog", ActivityAuthorization.this)) {
            serializeText(String.valueOf(0),"Dialog", "Dialog");
        }
    }

    private void lastRed(TextView view) {
        String text = view.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(redColorSpan, text.length() - 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannableString);
    }

    public void statusAuthorization(EditText editText, TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check the length of the text and set the appropriate image
                if ((s.length() == 0) ) {
                    editText.setBackgroundResource(R.drawable.form_authorization);
                    setTextColor(textView, R.color.text);
                } else {
                    editText.setBackgroundResource(R.drawable.form_access_authorization);
                    setTextColor(textView, R.color.textSuccess);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this implementation
            }
        });
    }

    private void setTextColor(TextView textView, Integer color) {
        String hexColor = String.format("#%06X", ContextCompat.getColor(this, color) & 0xFFFFFF);
        textView.setTextColor(Color.parseColor(hexColor));
    }

    public void textFiltre(EditText editText, String regex, int maxLength) {
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            StringBuilder sb = new StringBuilder();
            for (int i = start; i < end; i++) {
                if (!Character.toString(source.charAt(i)).matches(regex) && sb.length() < maxLength) {
                    sb.append(source.charAt(i));
                }
                else {
                    sb.append("");
                }
            }
            return sb.toString();
        };

        editText.setFilters(new InputFilter[]{filter});
    }


    private void Serialise() {
        this.runOnUiThread(doSerialise);
    }

    private Runnable doSerialise = new Runnable() {
        public void run()
        {
            Field[] fields = R.string.class.getFields();
            for (Field field : fields) {
                try {
                    int resId = field.getInt(null);
                    String value = getResources().getString(resId);
                    serializeText(value, String.valueOf(resId), String.valueOf(resId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (serialise != null) {
                serialise.cancel();
                serialise = null;
            }
        }
    };


    public String replaceUnderscoresWithHyphens(EditText editText) {
        String text = editText.getText().toString();
        String replacedText = text.replace("_", "");
        return replacedText;
    }
}
