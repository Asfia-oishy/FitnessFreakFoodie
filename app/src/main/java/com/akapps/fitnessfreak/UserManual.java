package com.akapps.fitnessfreak;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserManual extends AppCompatActivity {
    TextView exitbutn;
    Button gotIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);

        exitbutn=findViewById(R.id.goback);
        gotIt=findViewById(R.id.gotit);
        exitbutn.setOnClickListener(v -> back_pressed());
        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserManual.this,LoginPage.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        back_pressed();
    }

    void back_pressed()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(UserManual.this)
                .setMessage("Action will exit the page, sure to continue?")
                .setPositiveButton("Yes", (dialog, which) -> UserManual.this.finish())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create();

        alertDialog.show();
    }

}