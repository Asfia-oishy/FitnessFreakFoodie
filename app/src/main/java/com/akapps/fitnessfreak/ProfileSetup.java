package com.akapps.fitnessfreak;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ProfileSetup extends AppCompatActivity {
    LinearLayout ll1, ll2;
    RadioGroup radioGroup1, radioGroup2, radioGroup3;
    EditText age, goalweight;

    SwitchCompat switch1, switch2, switch3;
    EditText ed1, ed2, ed3;
    Button contin_btn, finish_btn;
    MaterialSpinner spinner1, spinner2;
    int age_val = 0;
    String gender = "";
    float height = 0.0f, weight = 0.0f, goal_weight = 0.0f;

    String[] strings1 = new String[]{"Sedentary(Desk job/Do not workout at all)", "Lightly Active(workout 1-3days/week)",
            "Moderately Active(workout 3-5days/week)", "Very Active(workout 6-7days/week)"};
    String[] strings2 = new String[]{"1kg/week(2.2lbs)", "0.75kg/week(1.65lbs)", "0.5kg/week(1.1lbs)", "0.25kg/week(0.55lbs)"};
    int selected_index1 = 0, selected_index2 = 0, taget_index = -1;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        radioGroup1 = findViewById(R.id.radioGroup);
        radioGroup2 = findViewById(R.id.gender);
        radioGroup3 = findViewById(R.id.radiogrt);
        age = findViewById(R.id.age);
        ll2.setVisibility(View.GONE);
        radioGroup1.check(R.id.radioButton);
        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        ed1 = findViewById(R.id.age2);
        ed2 = findViewById(R.id.age3);
        ed3 = findViewById(R.id.weight);
        contin_btn = findViewById(R.id.button7);
        finish_btn = findViewById(R.id.button8);
        spinner1 = findViewById(R.id.spinner2);
        spinner2 = findViewById(R.id.spinner);
        switch3 = findViewById(R.id.switch3);
        goalweight = findViewById(R.id.age4);

        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                ed2.setVisibility(View.GONE);
                ed1.setHint("Centimeter");
                //
            }
            else {
                ed2.setVisibility(View.VISIBLE);
                ed1.setHint("Feet");
                //
            }
        });

        spinner1.setVisibility(View.GONE);

        spinner1.setItems(strings2);
        spinner2.setItems(strings1);

        spinner1.setOnItemSelectedListener((view, position, id, item) -> selected_index1 = position);
        spinner2.setOnItemSelectedListener((view, position, id, item) -> selected_index2 = position);

        radioGroup3.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.radioButton6){
                spinner1.setVisibility(View.GONE);
                taget_index = 0;
            }

            else {
                spinner1.setVisibility(View.VISIBLE);
                if(checkedId == R.id.radioButton7){
                    taget_index = 1;
                }
                else {
                    taget_index = 2;
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.radioButton4){
                gender = "Male";
            }
            else {
                gender = "Female";
            }
        });


        contin_btn.setOnClickListener(v -> {
            if(gender.isEmpty()){
                Toast.makeText(ProfileSetup.this, "Select a Gender!", Toast.LENGTH_LONG).show();
                return;
            }

            String s1 = age.getText().toString();
            if(s1.isEmpty()){
                age.setError("Required! ");
                return;
            }
            try {
                age_val = Integer.parseInt(s1);
            }catch (Exception e){
                age.setError("Enter Valid Age!");
                return;
            }

            float val1, val2;
            s1 = ed1.getText().toString();

            if(s1.isEmpty()){
                ed1.setError("Required! ");
                return;
            }
            try {
                val1 = Float.parseFloat(s1);
            }catch (Exception e)
            {
                ed1.setError("Enter Valid height!");
                return;
            }

            if(!switch1.isChecked()){
                s1 = ed2.getText().toString();

                if(s1.isEmpty()){
                    ed2.setError("Required! ");
                    return;
                }
                try {
                    val2 = Float.parseFloat(s1);
                }catch (Exception e)
                {
                    ed2.setError("Enter Valid height!");
                    return;
                }
                val1 *= 12;
                val1+= val2;
                val1 *= 2.54;
            }

            height = new RoundFloat().getRound(val1);

            s1 = ed3.getText().toString();

            if(s1.isEmpty()){
                ed3.setError("Required! ");
                return;
            }
            try {
                val1 = Float.parseFloat(s1);
            }catch (Exception e)
            {
                ed3.setError("Enter Valid weight!");
                return;
            }

            if(switch2.isChecked()){
                val1 /= 2.205;
            }

            weight = new RoundFloat().getRound(val1);
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);
            radioGroup1.check(R.id.radioButton2);

        });


        finish_btn.setOnClickListener(v -> {
            String s1 = goalweight.getText().toString();

            if(s1.isEmpty()){
                goalweight.setError("Required! ");
                return;
            }
            float val1;
            try {
                val1 = Float.parseFloat(s1);
            }catch (Exception e)
            {
                goalweight.setError("Enter Valid weight!");
                return;
            }

            if(switch3.isChecked()){
                val1 /= 2.205;
            }

            goal_weight = new RoundFloat().getRound(val1);

            if(taget_index == -1){
                Toast.makeText(ProfileSetup.this, "Enter your Target!", Toast.LENGTH_LONG).show();
                return;
            }
            PopupLoading popupLoading = new PopupLoading(ProfileSetup.this);
            popupLoading.show();

            DetailsProfile detailsProfile = new DetailsProfile(age_val, taget_index, selected_index1, selected_index2,
                    gender, height, weight, goal_weight);

            myRef.child("AdditionalProfile").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).
                    setValue(detailsProfile);

            insertIntoDatabase(weight, goal_weight);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                popupLoading.dismiss();
                finish();
            }, 3000);

        });


    }


    @Override
    public void onBackPressed() {
        String str = "You Must Setup Your Profile First!";
        PopupDialog popupDialog = new PopupDialog(ProfileSetup.this, str);
        popupDialog.show();
    }



    void insertIntoDatabase(float current_weight, float goal_weight)
    {
        Date date = new Date(); //todays date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        ContentValues contentValues = new ContentValues();
        final String TABLE_NAME = "WeightTable";
        SQLiteDatabase db = ProfileSetup.this.openOrCreateDatabase("Fitness", Context.MODE_PRIVATE, null);
        contentValues.put("Current_weight", current_weight);
        contentValues.put("Goal_weight", goal_weight);
        contentValues.put("Day", day);
        contentValues.put("Month", month);
        contentValues.put("Year", year);
        db.insert(TABLE_NAME, null, contentValues);
    }

}