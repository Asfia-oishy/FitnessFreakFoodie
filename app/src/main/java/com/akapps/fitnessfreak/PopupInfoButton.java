package com.akapps.fitnessfreak;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class PopupInfoButton extends Dialog {
    String title, descrip;

    public PopupInfoButton(@NonNull Context context, String title,String descrip) {
        super(context);
        this.title = title;
        this.descrip = descrip;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_info_button);
        TextView t1, t2;
        Button b;
        t1=findViewById(R.id.showtitle);
        t2=findViewById(R.id.descriptn);
        t1.setText(title);
        t2.setText((descrip));
        b=findViewById(R.id.button18);
        b.setOnClickListener((View v) -> {
            this.dismiss();
        });
        this.setCancelable(false);
    }

}
