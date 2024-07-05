package com.akapps.fitnessfreak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragReport extends Fragment {
    TextView previous_month, next_month, monthtext;
    GridView gridView;
    Calendar calendar;
    int cur_month, cur_year;
    Context myContex;
    int beginning_of_month = 0;
    int ending_of_month = 0;
    View.OnClickListener listener1, listener2;
    ArrayList<CalorieClass> arrayList = new ArrayList<>();

    LinearLayout linearLayout, ll1, ll2;
    TextView no_date, no_text, intaken, burned, dates_text;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_report, container, false);
        monthtext = view.findViewById(R.id.textView13);
        previous_month = view.findViewById(R.id.textView11);
        next_month = view.findViewById(R.id.textView12);
        gridView = view.findViewById(R.id.grid_calendar);
        calendar = Calendar.getInstance(Locale.getDefault());
        dates_text = view.findViewById(R.id.textView73);
        linearLayout = view.findViewById(R.id.llo);
        ll1 = view.findViewById(R.id.lli);
        ll2 = view.findViewById(R.id.ll11);
        no_text = view.findViewById(R.id.textView69);

        linearLayout.setVisibility(View.GONE);
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        no_text.setVisibility(View.GONE);

        intaken = view.findViewById(R.id.textView75);
        burned = view.findViewById(R.id.textView77);


        no_date = view.findViewById(R.id.textView71);
        no_text = view.findViewById(R.id.textView69);

        Date date = new Date();
        calendar.setTime(date);
        cur_month = calendar.get(Calendar.MONTH);
        cur_year = calendar.get(Calendar.YEAR);

        edit_Calendar();


        final String TABLE_NAME = "CalorieTable";
        SQLiteDatabase db = myContex.openOrCreateDatabase("Fitness", Context.MODE_PRIVATE, null);
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



        listener1 = v -> {
            cur_month--;
            if(cur_month < 0){
                cur_month = 11;
                cur_year--;
            }
            calendar.set(Calendar.MONTH, cur_month);
            calendar.set(Calendar.YEAR, cur_year);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            edit_Calendar();
        };

        listener2 = v -> {
            cur_month++;
            if(cur_month > 11){
                cur_month = 0;
                cur_year++;
            }
            calendar.set(Calendar.MONTH, cur_month);
            calendar.set(Calendar.YEAR, cur_year);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            edit_Calendar();
        };

        previous_month.setOnClickListener(listener1);
        next_month.setOnClickListener(listener2);
        return view;
    }


    @SuppressLint("SimpleDateFormat")
    void edit_Calendar()
    {
        int num = 0;
        ArrayList<Date> list = new ArrayList<>();
        monthtext.setText(new SimpleDateFormat("MMM,yyyy").format(calendar.getTime()));
        calendar.set(Calendar.DAY_OF_MONTH, 1); //first day of month
        int count = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        calendar.set(Calendar.DAY_OF_MONTH, -count); //

        while (count> -1){
            list.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            num++;
            count--;
        }
        int month = calendar.get(Calendar.MONTH);
        beginning_of_month = num;
        while (month == calendar.get(Calendar.MONTH))
        {
            list.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            num++;
        }
        ending_of_month = num - 1;
        while (num < 42){
            list.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            num++;
        }

        gridView.setAdapter(new GridClass(list));
    }


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        myContex = context;
    }

    class GridClass extends BaseAdapter{
        ArrayList<Date> list;

        public GridClass(ArrayList<Date> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint({"ViewHolder", "InflateParams"}) View view = getLayoutInflater().inflate(R.layout.grid_calendardays, null);
            TextView textView = view.findViewById(R.id.textView70);

            Date date = list.get(position);
            Calendar newcalendar = Calendar.getInstance(Locale.getDefault());
            newcalendar.setTime(date);

            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);



            if (cur_month != newcalendar.get(Calendar.MONTH) || cur_year != newcalendar.get(Calendar.YEAR)) {
                textView.setTextColor(Color.parseColor("#C7D2E8"));
            }

            if(position < beginning_of_month){
                view.setOnClickListener(listener1);
            }
            else if(position > ending_of_month){
                view.setOnClickListener(listener2);
            }
            else {
                int d1 = newcalendar.get(Calendar.DAY_OF_MONTH);
                int d2 = newcalendar.get(Calendar.MONTH);
                int d3 = newcalendar.get(Calendar.YEAR);

                for(CalorieClass cl: arrayList){
                    if(cl.getDay() == d1 && cl.getMonth() == d2 && cl.getYear() == d3)
                    {
                        GradientDrawable gradientDrawable = new GradientDrawable();
                        gradientDrawable.setShape(GradientDrawable.OVAL);
                        gradientDrawable.setColor(Color.parseColor("#FFBB86FC")); //myContex.getColor(R.color.colorAccent);
                        textView.setBackground(gradientDrawable);
                        break;
                    }
                }


                view.setOnClickListener(v -> {
                    no_date.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    dates_text.setText(new SimpleDateFormat("dd MMM,yyyy").format(newcalendar.getTime()));
                    if(arrayList.size() == 0){
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.GONE);
                        no_text.setVisibility(View.VISIBLE);
                    }
                    else{
                        boolean isSet = false;
                        for(CalorieClass cl: arrayList){
                            if(cl.getDay() == d1 && cl.getMonth() == d2 && cl.getYear() == d3)
                            {
                                ll1.setVisibility(View.VISIBLE);
                                ll2.setVisibility(View.VISIBLE);
                                no_text.setVisibility(View.GONE);
                                intaken.setText(String.valueOf(cl.getIntake()));
                                burned.setText(String.valueOf(cl.getBurnt()));
                                isSet = true;
                                break;
                            }
                        }
                        if(!isSet){
                            ll1.setVisibility(View.GONE);
                            ll2.setVisibility(View.GONE);
                            no_text.setVisibility(View.VISIBLE);
                        }

                    }

                });
            }
            textView.setText(String.valueOf(newcalendar.get(Calendar.DATE)));

            return view;
        }
    }




}
