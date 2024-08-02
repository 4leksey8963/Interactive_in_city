package com.example.kostroma_geroicheskaya;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class firebaseResource extends BaseActivity{
    public static List<Bitmap> ImgPoints;
    List<StorageReference> storageReferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImgPoints = new ArrayList<>();
        storageReferences = new ArrayList<>();
        addingRef();
    }


    private void addingRef() {
        for (int i = 0; i < 6; i++) {
            storageReferences.add(FirebaseStorage.getInstance().getReference("images_points/point_map"+(i+1)+"_4.png"));
        }
    }

    private void setterForImg() {
        try {
            for (int i = 0; i < 6; i++) {
                File localfile = File.createTempFile("tempfile", ".png");
                storageReferences.get(i).getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                ImgPoints.add(BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            }
                        });
            }
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
