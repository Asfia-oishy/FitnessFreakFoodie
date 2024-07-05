package com.akapps.fitnessfreak;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class PopupDialog extends Dialog {
    String str;
    public PopupDialog(@NonNull Context context, String str) {
        super(context);
        this.str = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_dialog);
        TextView tx2;
        Button btn = findViewById(R.id.button4);
        tx2 = findViewById(R.id.textView12);
        this.setCancelable(false);
        tx2.setText(str);


        btn.setOnClickListener(v -> {
            this.dismiss();
        });
    }
}
