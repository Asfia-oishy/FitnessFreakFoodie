package com.akapps.fitnessfreak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    String str = "Fitness Freak";
    int num = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.opening);

        new Handler(Looper.getMainLooper()).postDelayed(runnable, 50);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(num < str.length())
            {
                String s1 = textView.getText().toString() + str.charAt(num);
                textView.setText(s1);
                num++;
                new Handler(Looper.getMainLooper()).postDelayed(runnable, 200);
            }
            else {
                textView.setTextColor(Color.parseColor("#FF000000"));
                new Handler(Looper.getMainLooper()).postDelayed(runnable1, 400);
                animation();
            }
        }
    };

    Runnable runnable1 = () -> {
        startActivity(new Intent(MainActivity.this, LoginPage.class));
        finish();
    };

    void animation()
    {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400);
        anim.setStartOffset(100);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.START_ON_FIRST_FRAME);
        textView.startAnimation(anim);
    }
}