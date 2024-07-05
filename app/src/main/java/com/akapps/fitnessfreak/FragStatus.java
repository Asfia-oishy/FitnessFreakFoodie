package com.akapps.fitnessfreak;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.securepreferences.SecurePreferences;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class FragStatus extends Fragment {
    float total_calval=0.0f;

    ProgressBar progressBar;
    LinearLayout linearLayout;
    Button insert_btn;
    Context myContext;
    TextView bmi_text, bmr_text, tdee_text, bmi_status, total_week_prog, left_prog, dates_text, taken_sofar, burned_sofar,final_show;
    EditText calories1, calories2;
    float left_cal=0.0f, diff=0.0f;
    TextView bmi_info, bmr_info, tdee_info;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    DetailsProfile detailsProfile;
    View[] views = new View[7];
    TextView[] dates = new TextView[7];
    TextView[] intakes = new TextView[7];
    TextView[] burnt = new TextView[7];

    ArrayList<CalorieClass > arrayList = new ArrayList<>();
    ArrayList<CalorieClass> list = new ArrayList<>();

    Calendar calendar;

    int selected_index = -1;
    int selected_index_id = -1;

    View.OnClickListener listener;

    boolean isMorethanCurrent = false;

    int todayDate, todayMonth, todayYear;

    float taken_t = 0.0f, burned_t = 0.0f;

    SharedPreferences sharedPreferences;

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("deprecation")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_status, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setBackgroundDrawable(pAnimation());
        linearLayout = view.findViewById(R.id.lin);
        insert_btn = view.findViewById(R.id.button17);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        bmi_text = view.findViewById(R.id.textView37);
        bmi_status = view.findViewById(R.id.textView81);
        bmr_text = view.findViewById(R.id.textView39);
        tdee_text = view.findViewById(R.id.textView56);
        final_show=view.findViewById(R.id.textView86);
        bmi_info=view.findViewById(R.id.textView47);
        bmr_info=view.findViewById(R.id.textView40);
        tdee_info=view.findViewById(R.id.textView57);

        total_week_prog = view.findViewById(R.id.textView59);
        left_prog = view.findViewById(R.id.textView62);

        taken_sofar = view.findViewById(R.id.textView590);
        burned_sofar = view.findViewById(R.id.textView591);

        dates_text = view.findViewById(R.id.textView65);
        calories1 = view.findViewById(R.id.calories1);
        calories2 = view.findViewById(R.id.calories2);

        calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        todayDate = calendar.get(Calendar.DAY_OF_MONTH);
        todayMonth = calendar.get(Calendar.MONTH);
        todayYear = calendar.get(Calendar.YEAR);

        sharedPreferences = new SecurePreferences(myContext);
        bmi_info.setOnClickListener(v -> {
            String s1="Body Mass Index(BMI)";
            String s2="Body mass index is a value derived from mass(weight) and height.BMI is a convenient rule of thumb used to broadly categorize a person as underweight,normal weight,overweight or obese.";
            PopupInfoButton popup = new PopupInfoButton(myContext,s1,s2);
            popup.show();
        });
        bmr_info.setOnClickListener(v -> {
            String s1="Basal Metabolic Rate(BMR)";
            String s2="Basal metabolic rate is a measurement of the number of calories needed to perform your body\\'s most basic functions,like breathing,circulation and cell production.";
            PopupInfoButton popup = new PopupInfoButton(myContext,s1,s2);
            popup.show();
        });
        tdee_info.setOnClickListener(v -> {
            String s1="Total Daily Energy Expenditure(TDEE)";
            String s2="Total daily energy expenditure is an estimation of how many calories you burn per day when exercise is taken into account.It is calculated by first figuring out your BMR,then multiplying that value by an activity multiplier.";
            PopupInfoButton popup = new PopupInfoButton(myContext,s1,s2);
            popup.show();
        });


        insert_btn.setOnClickListener(v -> {
            float f2;
            String s1 = calories1.getText().toString();
            if(s1.isEmpty()){
                calories1.setError("Required!");
                return;
            }


            String s2 = calories2.getText().toString();
            if(s2.isEmpty()){
                calories2.setError("Required!");
                return;
            }
            try {
                f2 = Float.parseFloat(s2);
            }catch (Exception e){
                calories2.setError("Enter Valid Number!");
                return;
            }

            String df = dates_text.getText().toString();
            if(df.isEmpty()){
                Toast.makeText(myContext, "No Date Selected!", Toast.LENGTH_LONG).show();
                return;
            }

            //float prev_intake = list.get(selected_index).getIntake();
            //float curr_intake = list.get(selected_index).getBurnt();
            //taken_t += Float.parseFloat(s1) + prev_intake;
            //burned_t += f2 - curr_intake;

            list.get(selected_index).setIntake(Float.parseFloat(s1));
            list.get(selected_index).setBurnt(f2);

            taken_t = 0.0f;
            burned_t = 0.0f;

            for(CalorieClass cl: list){
                taken_t+= cl.getIntake();
                burned_t+= cl.getBurnt();
            }

            burned_sofar.setText(String.valueOf(burned_t));
            taken_sofar.setText(String.valueOf(taken_t));
            left_cal=total_calval-taken_t+burned_t;
            if(left_cal >=0.0f)
            {
                final_show.setText("Excellent!You have more calories left");
                final_show.setTextColor(Color.parseColor("#0FAE15"));
            }
            else
            {
                final_show.setText("Sorry!You have exceeded calorie limit");
                final_show.setTextColor(Color.parseColor("#F1190A"));
            }
            left_prog.setText(String.valueOf(left_cal));


            if(isMorethanCurrent){
                Toast.makeText(myContext, "Successfully Inserted Temporary!", Toast.LENGTH_SHORT).show();
            }
            else{
                final String TABLE_NAME = "CalorieTable";
                SQLiteDatabase db = myContext.openOrCreateDatabase("Fitness", Context.MODE_PRIVATE, null);
                if(list.get(selected_index).getId() == -1){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("IntakeCal", Float.parseFloat(s1));
                    contentValues.put("BurntCal", f2);
                    contentValues.put("Day", list.get(selected_index).getDay());
                    contentValues.put("Month", list.get(selected_index).getMonth());
                    contentValues.put("Year", list.get(selected_index).getYear());
                    contentValues.put("DayofWeek", list.get(selected_index).getType());
                    db.insert(TABLE_NAME, null, contentValues);
                }
                else{
                    String COMM = "UPDATE "+ TABLE_NAME + " SET IntakeCal = "+ Float.parseFloat(s1) + ", BurntCal = "+ f2 + " WHERE " +
                            "ID = "+ list.get(selected_index).getId();
                    db.execSQL(COMM);
                }
                Toast.makeText(myContext, "Successfully Updated!", Toast.LENGTH_LONG).show();


            }
            intakes[selected_index].setText(s1);
            burnt[selected_index].setText(s2);



        });

        listener = v -> {
            for(int i=0; i<7; i++){
                if(v == views[i]){
                    selected_index = i;
                    if(i > selected_index_id){
                        isMorethanCurrent = true;
                    }
                    break;
                }
            }

            dates_text.setText(getDate(list.get(selected_index).getDay(),
                    list.get(selected_index).getMonth(), list.get(selected_index).getYear()));

            if(selected_index_id == -1) return;

            calories1.setText(String.valueOf(list.get(selected_index).getIntake()));
            calories2.setText(String.valueOf(list.get(selected_index).getBurnt()));
        };

        for(int i=0 ; i< 7; i++){
            LayoutInflater inflater1 = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View view1 = inflater1.inflate(R.layout.progress_day, null);
            linearLayout.addView(view1);
            setMargins(view1, 10,0,10, 0);
            views[i] = view1;
            views[i].setOnClickListener(listener);
            dates[i] = view1.findViewById(R.id.textView73);
            intakes[i] = view1.findViewById(R.id.textView75);
            burnt[i] = view1.findViewById(R.id.textView77);
        }


        try {
            final String TABLE_NAME = "CalorieTable";
            SQLiteDatabase db = myContext.openOrCreateDatabase("Fitness", Context.MODE_PRIVATE, null);
            String COMM1 = "SELECT COUNT(*) FROM "+ TABLE_NAME;
            String COMM2 = "SELECT * FROM "+ TABLE_NAME;
            Cursor cursor = db.rawQuery(COMM1, null);
            cursor.moveToFirst();
            int icount = cursor.getInt(0);
            if(icount > 0){
                cursor = db.rawQuery(COMM2, null);
                int intake_index = cursor.getColumnIndex("IntakeCal");
                int burned_index = cursor.getColumnIndex("BurntCal");
                int id_index = cursor.getColumnIndex("ID");
                int day_index = cursor.getColumnIndex("Day");
                int month_index = cursor.getColumnIndex("Month");
                int year_index = cursor.getColumnIndex("Year");
                int day_of_week = cursor.getColumnIndex("DayofWeek");
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    CalorieClass calorieClass = new CalorieClass(cursor.getInt(id_index),
                            cursor.getInt(day_index),
                            cursor.getInt(month_index),
                            cursor.getInt(year_index),
                            cursor.getInt(day_of_week),
                            cursor.getFloat(intake_index),
                            cursor.getFloat(burned_index));

                    arrayList.add(calorieClass);
                    cursor.moveToNext();
                }

            }
            cursor.close();
            db.close();

            calendar.setTime(new Date());
            if(list.size() > 0) list.clear();

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            if(arrayList.size() == 0){
                for(int i=0; i<7; i++){
                    int d1 = calendar.get(Calendar.DAY_OF_MONTH);
                    int d2 = calendar.get(Calendar.MONTH);
                    int d3 = calendar.get(Calendar.YEAR);
                    if(d1 == todayDate && d2 == todayMonth && d3 == todayYear){
                        selected_index_id = i;
                    }
                    String date = getDate(d1, d2, d3);
                    dates[i].setText(date);
                    intakes[i].setText("Not Found!");
                    burnt[i].setText("Not Found!");
                    list.add(new CalorieClass(-1, d1, d2, d3, 0, 0.0f, 0.0f));
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }

            else{
                for(int i=0; i<7; i++){
                    boolean isFound = false;
                    int d1 = calendar.get(Calendar.DAY_OF_MONTH);
                    int d2 = calendar.get(Calendar.MONTH);
                    int d3 = calendar.get(Calendar.YEAR);
                    String date = getDate(d1, d2, d3);
                    if(d1 == todayDate && d2 == todayMonth && d3 == todayYear){
                        selected_index_id = i;
                    }
                    dates[i].setText(date);
                    for(int j=0; j< arrayList.size(); j++){
                        if(arrayList.get(j).getDay() == d1 && arrayList.get(j).getMonth() == d2 && arrayList.get(j).getYear()
                                == d3){

                            list.add(new CalorieClass(arrayList.get(j).getId(), d1, d2, d3, calendar.get(Calendar.DAY_OF_WEEK),
                                    arrayList.get(j).getIntake(), arrayList.get(j).getBurnt()));
                            isFound = true;
                            intakes[i].setText(arrayList.get(j).getIntake() + " Calories");
                            burnt[i].setText(arrayList.get(j).getBurnt() + " Calories");
                            break;
                        }
                    }
                    if(!isFound){
                        intakes[i].setText("Not Found!");
                        burnt[i].setText("Not Found!");
                        list.add(new CalorieClass(-1, d1, d2, d3, calendar.get(Calendar.DAY_OF_MONTH), 0.0f, 0.0f));
                    }
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }


        }catch (Exception e){
            Toast.makeText(myContext, "Error! "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        for(CalorieClass cc: list){
            taken_t += cc.getIntake();
            burned_t+= cc.getBurnt();
        }

        taken_sofar.setText(String.valueOf(taken_t));
        burned_sofar.setText(String.valueOf(burned_t));


        diff=taken_t-burned_t;

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        myRef.child("AdditionalProfile").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(!snapshot.exists())
                        {
                            Toast.makeText(myContext, "Please Complete Setting Up Your Profile First!"
                                    , Toast.LENGTH_SHORT).show();
                            ((CerterPage) requireActivity()).setup_profile();
                            return;
                        }

                        detailsProfile = snapshot.getValue(DetailsProfile.class);
                        if(detailsProfile == null) return;
                        float f1 = detailsProfile.getHeight() / 100;
                        float bmi = detailsProfile.getWeight() / (f1 * f1);
                        bmi = new RoundFloat().getRound(bmi);
                        bmi_text.setText(String.valueOf(bmi));
                        //Integer bmi_int=(Integer)bmi;
                        if(bmi<15.0f)
                        {
                            progressBar.setProgress(0);
                        }
                        else if(bmi>40.0f)
                        {
                            progressBar.setProgress(100);
                        }
                        else if(bmi >=15.0f && bmi<18.5f)
                        {
                            float bmi_prog=((bmi-14.0f)/3.5f)*13.0f;
                            int b_progbar;
                            b_progbar=(int)bmi_prog;
                            progressBar.setProgress(b_progbar);
                        }
                        else if(bmi >=18.f && bmi<25.0f)
                        {
                            float bmi_prog=((bmi-15.0f)/10.0f)*37.5f;
                            int b_progbar;
                            b_progbar=(int)bmi_prog;
                            progressBar.setProgress(b_progbar);
                        }
                            else if(bmi >=25.0f && bmi<30.0f)
                        {
                            float bmi_prog=((bmi-15.0f)/15.0f)*62.25f;
                            int b_progbar;
                            b_progbar=(int)bmi_prog;
                            progressBar.setProgress(b_progbar);
                        }
                        else if(bmi >=30.0f && bmi<35.0f)
                        {
                            float bmi_prog=((bmi-15.0f)/21.0f)*86.25f;
                            int b_progbar;
                            b_progbar=(int)bmi_prog;
                            progressBar.setProgress(b_progbar);
                        }
                        else if(bmi >=35.0f && bmi<40.0f)
                        {
                            float bmi_prog=((bmi-15.0f)/25.0f)*100.0f;
                            int b_progbar;
                            b_progbar=(int)bmi_prog;
                            progressBar.setProgress(b_progbar);
                        }

                        String bmi_state;
                        if(bmi<15.0f)
                        {
                            bmi_state="Very Underweight";
                            bmi_status.setText(bmi_state);
                            bmi_status.setTextColor(Color.parseColor("#FF6200EE"));
                        }
                        else if(bmi>=15.0f && bmi<=18.5f)
                        {
                            bmi_state="Underweight";
                            bmi_status.setText(bmi_state);
                            bmi_status.setTextColor(Color.parseColor("#FF3700B3"));
                        }
                        else if(bmi>18.5f && bmi<25.0f)
                        {
                            bmi_state="Normal";
                            bmi_status.setText(bmi_state);
                            bmi_status.setTextColor(Color.parseColor("#0FAE15"));
                        }
                        else if(bmi>=25.0f && bmi<30.0f)
                        {
                            bmi_state="Overweight";
                            bmi_status.setText(bmi_state);
                            bmi_status.setTextColor(Color.parseColor("#F8BF31"));
                        }
                        else if(bmi>=30.0f && bmi<35.0f)
                        {
                            bmi_state="Obese Class 1";
                            bmi_status.setText(bmi_state);
                            bmi_status.setTextColor(Color.parseColor("#F66C0F"));
                        }
                        else if(bmi>=35.0f && bmi<40.0f)
                        {
                            bmi_state="Obese Class 2";
                            bmi_status.setText(bmi_state);
                            bmi_status.setTextColor(Color.parseColor("#BD4037"));
                        }
                        else
                        {
                            bmi_state="Obese Class 3";
                            bmi_status.setText(bmi_state);
                            bmi_status.setTextColor(Color.parseColor("#F1190A"));
                        }


                        float bmr;
                        if(detailsProfile.getGender().equals("Male")){
                            bmr = (float) ((10 * detailsProfile.getWeight()) + (6.25 * detailsProfile.getHeight()) -
                                    (5 * detailsProfile.getAge()) + 5);
                        }
                        else {
                            bmr = (float) ((10* detailsProfile.getWeight()) + (6.25 * detailsProfile.getHeight()) -
                                    (5* detailsProfile.getAge()) - 161 );
                        }

                        bmr = new RoundFloat().getRound(bmr);
                        bmr_text.setText(String.valueOf(bmr));

                        float tdee;

                        if(detailsProfile.getActivity_index() == 0){
                            tdee = bmr * 1.2f ;
                        }
                        else if(detailsProfile.getActivity_index() == 1){
                            tdee = bmr * 1.375f;
                        }
                        else  if(detailsProfile.getActivity_index() == 2){
                            tdee = bmr * 1.55f;
                        }
                        else{
                            tdee = bmr* 1.7f;
                        }

                        tdee = new RoundFloat().getRound(tdee);
                        tdee_text.setText(String.valueOf(tdee));

                        //bmi_status.setTextColor(Color.BLACK);
                        //bmi_status.setTextColor(Color.parseColor("#2196F3"));
                        //Log.i("dddd", String.valueOf(detailsProfile.getTarget_index()));
                        float total_cal;
                        if(detailsProfile.getTarget_type() == 0)
                        {
                            total_cal = tdee * 7;
                        }
                        else if(detailsProfile.getTarget_type() == 1)
                        {
                            if(detailsProfile.getTarget_index() == 0){
                                total_cal=(tdee-1100)*7;
                            }
                           else if(detailsProfile.getTarget_index() == 1){
                                total_cal=(tdee-825)*7;
                            }
                            else if(detailsProfile.getTarget_index() == 2){
                                total_cal=(tdee-550)*7;
                            }
                            else
                            {
                                total_cal=(tdee-275)*7;
                            }
                        }
                        else{
                            //TODO
                            if(detailsProfile.getTarget_index() == 0){
                                total_cal=(tdee+1100)*7;
                            }
                            else if(detailsProfile.getTarget_index() == 1){
                                total_cal=(tdee+825)*7;
                            }
                            else if(detailsProfile.getTarget_index() == 2){
                                total_cal=(tdee+550)*7;
                            }
                            else
                            {
                                total_cal=(tdee+275)*7;
                            }
                        }

                        total_calval=total_cal;
                        total_calval = new RoundFloat().getRound(total_calval);
                        total_week_prog.setText(String.valueOf(total_calval));
                        //total_week_prog.setText(total_cal+" Calories");

                        left_cal=total_calval-diff;
                        left_cal = new RoundFloat().getRound(left_cal);
                        left_prog.setText(String.valueOf(left_cal));

                        if(left_cal>=0.0f)
                        {
                            final_show.setText("Excellent!You have more calories left");
                            final_show.setTextColor(Color.parseColor("#0FAE15"));
                        }
                        else
                        {
                            final_show.setText("Sorry!You have exceeded calorie limit");
                            final_show.setTextColor(Color.parseColor("#F1190A"));
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    String getDate(int day, int month, int year){
        month = month + 1;
        StringBuilder sw = new StringBuilder(String.valueOf(month));
        if(month < 10) sw.insert(0, "0");
        StringBuilder dw = new StringBuilder(String.valueOf(day));
        if(day < 10){
            dw.insert(0, "0");
        }
        return dw.toString() + "/"+ sw.toString()+ "/"+ year;
    }



    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    private AnimationDrawable pAnimation(){

        GradientDrawable rainbow1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.BLUE, Color.parseColor("#009106"), Color.parseColor("#FF9800"),
                        Color.parseColor("#F44336"), Color.RED});
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(rainbow1, 100);
        animation.setOneShot(false);
        return animation;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        myContext = context;
    }
}
