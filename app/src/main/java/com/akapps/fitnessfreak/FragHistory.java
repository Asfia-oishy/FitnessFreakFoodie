package com.akapps.fitnessfreak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class FragHistory extends Fragment {
    ListView listView;
    Context myContext;
    ArrayList<WeightClass> arrayList = new ArrayList<>();
    ArrayList<DateVal> dates = new ArrayList<>();
    AutoCompleteTextView spinner;
    GraphView graphView;
    int selected_position = -1;
    Calendar calendar;
    String[] items = new String[]{"This Week", "This Month", "Last Three Months", "Last Six Months", "Last Year"};
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_history, container, false);
        listView = view.findViewById(R.id.list_view);
        spinner = view.findViewById(R.id.filled_exposed_dropdown1);
        graphView = view.findViewById(R.id.graph);
        calendar = Calendar.getInstance();
        ArrayAdapter<String > arrayAdapter = new ArrayAdapter<>(myContext, R.layout.dropdown_menu_popup_item, items);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemClickListener((parent, view1, position, id) -> {
            selected_position = position;
            if(position == 0){
                indexedSearch(7);
            }
            else if(position == 1){
                indexedSearch(30);
            }
            else if(position == 2){
                indexedSearch(90);
            }
            else if(position == 3){
                indexedSearch(180);
            }
            else{
                indexedSearch(365);
            }
        });

        final String TABLE_NAME = "WeightTable";
        SQLiteDatabase db = myContext.openOrCreateDatabase("Fitness", Context.MODE_PRIVATE, null);
        String COMMAND1 = "SELECT COUNT(*) FROM "+ TABLE_NAME;
        String COMMAND2 = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(COMMAND1, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);

        if(count > 0){
            cursor = db.rawQuery(COMMAND2, null);
            int day_index = cursor.getColumnIndex("Day");
            int month_index = cursor.getColumnIndex("Month");
            int year_index = cursor.getColumnIndex("Year");
            int cwei_index = cursor.getColumnIndex("Current_weight");
            int gwei_index = cursor.getColumnIndex("Goal_weight");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                WeightClass weightClass = new WeightClass(cursor.getInt(day_index),
                        cursor.getInt(month_index),
                        cursor.getInt(year_index),
                        cursor.getFloat(cwei_index),
                        cursor.getFloat(gwei_index));
                arrayList.add(weightClass);
                dates.add(new DateVal(weightClass.getDay(), weightClass.getMonth(), weightClass.getYear()));
                cursor.moveToNext();
            }

        }


        indexedSearch(7);
        cursor.close();
        db.close();
        return view;
    }


    void indexedSearch(int number)
    {

        ArrayList<WeightClass> mList = new ArrayList<>();
        ArrayList<DataPoint> dList = new ArrayList<>();
        ArrayList<DataPoint> gList = new ArrayList<>();

        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -6);
        Date start_date = calendar.getTime();
        for(int i=0; i< number; i++){
            int d1 = calendar.get(Calendar.DAY_OF_MONTH);
            int d2 = calendar.get(Calendar.MONTH);
            int d3 = calendar.get(Calendar.YEAR);
            DateVal dateVal = new DateVal(d1, d2, d3);
            if(dates.contains(dateVal)){
                int index = dates.indexOf(dateVal);
                WeightClass weightClass = arrayList.get(index);
                mList.add(weightClass);
                Calendar nCalen = Calendar.getInstance();
                nCalen.set(d3, d2, d1, 0,0);
                dList.add(new DataPoint(nCalen.getTime(), weightClass.getCurrent_weight()));
                gList.add(new DataPoint(nCalen.getTime(), weightClass.getGoal_weight()));
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Date end_date = calendar.getTime();
        graphView.removeAllSeries();
        DataPoint[] dataPoint = dList.toArray(new DataPoint[0]);
        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoint);
        lineGraphSeries.setTitle("Weight Curve");
        lineGraphSeries.setColor(Color.BLUE);
        lineGraphSeries.setDrawDataPoints(true);
        lineGraphSeries.setDataPointsRadius(10);
        lineGraphSeries.setThickness(8);


        DataPoint[] dataPoint1 = gList.toArray(new DataPoint[0]);
        LineGraphSeries<DataPoint> lineGraphSeries1 = new LineGraphSeries<>(dataPoint1);
        lineGraphSeries1.setTitle("Goal Weight Curve");
        lineGraphSeries1.setColor(Color.RED);
        lineGraphSeries1.setDrawDataPoints(true);
        lineGraphSeries1.setDataPointsRadius(10);
        lineGraphSeries1.setThickness(8);


        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(myContext));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);


        graphView.getGridLabelRenderer().setHumanRounding(true);

        graphView.getViewport().setMinX(start_date.getTime());
        graphView.getViewport().setMaxX(end_date.getTime());
        graphView.getViewport().setXAxisBoundsManual(true);

        graphView.getViewport().setScalableY(true);

        graphView.addSeries(lineGraphSeries1);
        graphView.addSeries(lineGraphSeries);


        listView.setAdapter(new ListClass(mList));
    }


    class ListClass extends BaseAdapter{

        ArrayList<WeightClass > list;

        public ListClass(ArrayList<WeightClass> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if(list.size() == 0) return 1;
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

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(list.size() == 0){
                return getLayoutInflater().inflate(R.layout.grid_null, null);
            }

            View view = getLayoutInflater().inflate(R.layout.list_itemtoshow, null);
            TextView tx1, tx2;
            tx1 = view.findViewById(R.id.textView78);
            tx2 = view.findViewById(R.id.textView79);
            int month = list.get(position).getMonth() + 1;
            String sw = String.valueOf(month);
            if(month < 10) sw = "0"+sw;
            String dw = String.valueOf(list.get(position).getDay());
            if(list.get(position).getDay() < 10){
                dw = "0"+dw;
            }
            String str = dw + "/"+ sw + "/"+ list.get(position).getYear();
            tx1.setText(str);
            String sty = "Weight: "+ list.get(position).getCurrent_weight() + "kg";
            tx2.setText(sty);
            return view;
        }
    }


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        myContext = context;
    }



    public static class DateVal{
        int day, month, year;

        public DateVal(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        public int getDay() {
            return day;
        }

        public int getMonth() {
            return month;
        }

        public int getYear() {
            return year;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DateVal dateVal = (DateVal) o;
            return day == dateVal.day &&
                    month == dateVal.month &&
                    year == dateVal.year;
        }

        @Override
        public int hashCode() {
            return Objects.hash(day, month, year);
        }

    }
}
