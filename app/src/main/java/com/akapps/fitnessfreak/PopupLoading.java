package com.akapps.fitnessfreak;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class PopupLoading extends Dialog {
    public PopupLoading(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_progress);
        this.setCancelable(false);
    }
}
