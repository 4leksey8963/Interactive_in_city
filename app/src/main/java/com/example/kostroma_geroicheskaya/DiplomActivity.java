package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class DiplomActivity extends BaseActivity {
    Button back, save;
    ImageView imageDiplom;

    String name, lastname, school, clas, marsh;

    String folderToSave = Environment.getExternalStorageDirectory().toString() + "/Pictures/Heroic_Kostroma";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diplom);
        setStatusBarColor(R.color.darkGreen);
        back = (Button) findViewById(R.id.Back);
        save = (Button) findViewById(R.id.saveButton);
        imageDiplom = (ImageView) findViewById(R.id.imageDiplom);
        String userInfo = deserializeText(getString(R.string.User), getString(R.string.User), DiplomActivity.this);
        String[] parts = userInfo.split("_");
        name = parts[0];
        lastname = parts[1];
        school = parts[2];
        clas = parts[3];
        marsh = deserializeText("marsh_name", "marsh_name", DiplomActivity.this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiplomActivity.this, RewardsActivity.class);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save();
            }
        });
        makeDiplom();
    }
    private void Save(){
        if (createFolder()) {
            try {
                OutputStream fOut = null;
                Time time = new Time();
                time.setToNow();
                String fileName = "KG_Diplom" + Integer.toString(time.year) + Integer.toString(time.month + 1) + Integer.toString(time.monthDay) + Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second) + ".jpg";
                File file = new File(folderToSave, fileName);
                fOut = new FileOutputStream(file);

                Bitmap bitmap = ((BitmapDrawable) imageDiplom.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();
                Toast.makeText(this, "Диплом сохранён как JPG", Toast.LENGTH_SHORT).show();
                File imageFile = new File(folderToSave, fileName);
                MediaScannerConnection.scanFile(this,
                        new String[]{imageFile.getAbsolutePath()},
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {

                            }
                        });
            } catch (Exception e) {
                Toast.makeText(this, "Не удалось сохранить диплом.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void makeDiplom(){
        try {
            Drawable drawable = getResources().getDrawable(R.drawable.diplom);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(30);
            paint.setTypeface(ResourcesCompat.getFont(this, R.font.old_soviet));

            canvas.drawText(name + " " + lastname + getString(R.string.school), 277, 398, paint);
            canvas.drawText(school + getString(R.string.clas) + " " + clas, 277, 478, paint);
            canvas.drawText(marsh, 636, 678, paint);
            imageDiplom.setImageBitmap(bitmap);
        }
        catch (Exception e){
            Toast.makeText(this, "Произошла ошибка создания диплома.", Toast.LENGTH_LONG).show();;
        }
    }

    private boolean createFolder(){
        File folderPath = new File(folderToSave);

        if (!folderPath.exists()) {
            if (folderPath.mkdirs()) {
                return true;
            } else {
                Toast.makeText(this, "Не удалось создать папку для диплома", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DiplomActivity.this, RewardsActivity.class);
        startActivity(intent);
    }
}
