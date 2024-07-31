package com.example.kostroma_geroicheskaya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {
    private int imageResource;
    private String textMedal;
    Button btn;

    public CustomDialogFragment(int imageResource, String textMedal) {
        this.imageResource = imageResource;
        this.textMedal = textMedal;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        ImageView imageView = dialogView.findViewById(R.id.imageView);
        TextView text = dialogView.findViewById(R.id.textView);
        btn = dialogView.findViewById(R.id.button);
        text.setText(textMedal);
        imageView.setImageResource(imageResource);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder
                .setView(dialogView)
                .create();
    }
}
