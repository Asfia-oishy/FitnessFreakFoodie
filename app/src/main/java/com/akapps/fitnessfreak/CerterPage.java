package com.akapps.fitnessfreak;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.securepreferences.SecurePreferences;

import java.util.ArrayList;
import java.util.Objects;

public class CerterPage extends AppCompatActivity {
    TabLayout tabLayout;
    TextView back;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certer_page);
        tabLayout = findViewById(R.id.tabLayout);
        back=findViewById(R.id.textView13);
        //for fragment setup
        getSupportFragmentManager().beginTransaction().add(R.id.frame, new FragStatus()).commit();
        sharedPreferences = new SecurePreferences(this);

        String email = sharedPreferences.getString("emailval", "");
        if(email.isEmpty() || !email.equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()))
        {
            //I love you my husband ....hehe
            setupSqliteDatabase();
            sharedPreferences.edit().putString("emailval", Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).apply();

        }
        back.setOnClickListener(v -> back_pressed());


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragStatus()).commit();
                }
                else if(tab.getPosition() == 1){
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, new FragReport()).commit();
                }
                else if(tab.getPosition() == 2){
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, new FragHistory()).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, new FragProfile()).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        back_pressed();
    }

    void back_pressed()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(CerterPage.this)
                .setMessage("Action will exit the app, sure to continue?")
                .setPositiveButton("Yes", (dialog, which) -> CerterPage.this.finish())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create();

        alertDialog.show();
    }


    public void setup_profile()
    {
        startActivity(new Intent(CerterPage.this, ProfileSetup.class));
    }


    public void logoutOption()
    {
        sharedPreferences.edit().putBoolean("prevlogin",false).apply();
        mAuth.signOut();
        startActivity(new Intent(CerterPage.this, LoginPage.class));
        finish();
    }


    void setupSqliteDatabase()
    {
        final String TABLE_NAME1 = "WeightTable";
        final String TABLE_NAME2 = "CalorieTable";

        SQLiteDatabase db = CerterPage.this.openOrCreateDatabase("Fitness", Context.MODE_PRIVATE, null);

        String COMMAND_TO_CREATE1 = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME1 + "(ID INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, Current_weight REAL, Goal_weight REAL, Day INTEGER, Month INTERGER, Year INTEGER)";
        String COMMAND_TO_CREATE2 = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME2 + "(ID INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, IntakeCal REAL, BurntCal REAL, Day INTEGER, Month INTEGER, Year INTEGER, DayofWeek INTEGER, " +
                "Type INTEGER)";

        String COMMAND_TO_DELETE1 = "DROP TABLE IF EXISTS "+ TABLE_NAME1;
        String COMMAND_TO_DELETE2 = "DROP TABLE IF EXISTS "+ TABLE_NAME2;
        db.execSQL(COMMAND_TO_DELETE1);
        db.execSQL(COMMAND_TO_DELETE2);
        db.execSQL(COMMAND_TO_CREATE1);
        db.execSQL(COMMAND_TO_CREATE2);
    }

}