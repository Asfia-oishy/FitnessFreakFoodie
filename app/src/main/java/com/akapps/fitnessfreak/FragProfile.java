package com.akapps.fitnessfreak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragProfile extends Fragment {
    TextView main_edit, main_save, main_cancel, weight_edit, weight_save, weight_cancel;
    CircleImageView circleImageView;
    Button upload_btnl;

    Bitmap bitmap;

    EditText name, email, phone_number;

    TextInputLayout cweight, gweight, age, height;

    RadioGroup gender, target;
    RadioButton[] radioButtons = new RadioButton[5];

    MaterialSpinner spinner1, spinner2;

    Button edit_btn, save_btn, cancel_btn;

    Button logout_btn, restore_btn;

    Context myContext;

    String sname="", semail="", sphonenum="";

    String pwei = "", cwei = "";

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    StorageReference stRef = FirebaseStorage.getInstance().getReference();

    String[] strings1 = new String[]{"Sedentary(Desk job/Do not workout at all)", "Lightly Active(workout 1-3days/week)",
            "Moderately Active(workout 3-5days/week)", "Very Active(workout 6-7days/week)"};
    String[] strings2 = new String[]{"1kg/week(2.2lbs)", "0.75kg/week(1.65lbs)", "0.5kg/week(1.1lbs)", "0.25kg/week(0.55lbs)"};

    final long TEN_MEGABYTE = 10* 1024* 1024;

    String sage ="", sheigh = "";

    int gender_no = 0, sindex1 = 0, sindex2 = 0, sindex3 = 0;

    int gender_val = 0, taget_index = 0;

    LinearLayout linearLayout;

    @SuppressLint("IntentReset")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_profile, container, false);
        main_edit = view.findViewById(R.id.textView380);
        main_save = view.findViewById(R.id.textView460);
        main_cancel = view.findViewById(R.id.textView390);
        weight_edit = view.findViewById(R.id.textView38);
        weight_save = view.findViewById(R.id.textView46);
        weight_cancel = view.findViewById(R.id.textView39);
        upload_btnl = view.findViewById(R.id.button19);
        name = view.findViewById(R.id.textView30);
        email = view.findViewById(R.id.textView32);
        phone_number = view.findViewById(R.id.textView34);
        linearLayout = view.findViewById(R.id.llnui);
        circleImageView = view.findViewById(R.id.profile_image);
        cweight = view.findViewById(R.id.outlinedTextField3);
        gweight = view.findViewById(R.id.outlinedTextField4);
        age = view.findViewById(R.id.outlinedTextField1);
        height = view.findViewById(R.id.outlinedTextField2);
        gender = view.findViewById(R.id.gender);
        target = view.findViewById(R.id.radiou);
        spinner1 = view.findViewById(R.id.spinner3);
        spinner2 = view.findViewById(R.id.spinner4);
        edit_btn = view.findViewById(R.id.button13);
        save_btn = view.findViewById(R.id.button6);
        cancel_btn = view.findViewById(R.id.button9);
        radioButtons[0] = view.findViewById(R.id.radioButton4);
        radioButtons[1] = view.findViewById(R.id.radioButton5);
        radioButtons[2] = view.findViewById(R.id.radioButton6);
        radioButtons[3] = view.findViewById(R.id.radioButton7);
        radioButtons[4] = view.findViewById(R.id.radioButton8);

        logout_btn = view.findViewById(R.id.button15);
        restore_btn = view.findViewById(R.id.button14);

        main_save.setVisibility(View.GONE);
        main_cancel.setVisibility(View.GONE);
        upload_btnl.setVisibility(View.GONE);
        weight_save.setVisibility(View.GONE);
        weight_cancel.setVisibility(View.GONE);
        save_btn.setVisibility(View.GONE);
        cancel_btn.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        main_edit.setEnabled(false);
        weight_edit.setEnabled(false);

        spinner1.setItems(strings1);
        spinner2.setItems(strings2);
        spinner1.setEnabled(false);
        spinner2.setEnabled(false);

        //retrieve main profile
        myRef.child("Mainprofile").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Mainprofile mainprofile = snapshot.getValue(Mainprofile.class);
                        if(mainprofile == null) return;
                        main_edit.setEnabled(true);
                        sname = mainprofile.getName();
                        name.setText(sname);
                        name.setEnabled(false);
                        semail = mainprofile.getEmail();
                        email.setText(semail);
                        email.setEnabled(false);
                        sphonenum = mainprofile.getPhonenumber();
                        phone_number.setText(sphonenum);
                        phone_number.setEnabled(false);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        //additional profile
        myRef.child("AdditionalProfile").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        weight_edit.setEnabled(true);
                        DetailsProfile detailsProfile = snapshot.getValue(DetailsProfile.class);
                        if(detailsProfile == null) return;
                        cwei = String.valueOf(detailsProfile.getWeight());
                        Objects.requireNonNull(cweight.getEditText()).setText(cwei);
                        cweight.setEnabled(false);

                        pwei = String.valueOf(detailsProfile.getGoal_weight());
                        Objects.requireNonNull(gweight.getEditText()).setText(pwei);
                        gweight.setEnabled(false);

                        sage = String.valueOf(detailsProfile.getAge());
                        Objects.requireNonNull(age.getEditText()).setText(sage);
                        age.setEnabled(false);

                        sheigh = String.valueOf(detailsProfile.getHeight());
                        Objects.requireNonNull(height.getEditText()).setText(sheigh);
                        height.setEnabled(false);

                        if(detailsProfile.getGender().equals("Male")){
                            gender.check(R.id.radioButton4);
                            gender_no = 0;
                        }
                        else{
                            gender_no = 1;
                            gender.check(R.id.radioButton5);
                        }

                        sindex1 = detailsProfile.getActivity_index();
                        spinner1.setSelectedIndex(sindex1);
                        sindex2 = detailsProfile.getTarget_index();
                        spinner2.setSelectedIndex(sindex2);

                        if(detailsProfile.getTarget_type() == 0){
                            sindex3 = 0;
                            target.check(R.id.radioButton6);
                        }
                        else if(detailsProfile.getTarget_type() == 1){
                            sindex3 = 1;
                            target.check(R.id.radioButton7);
                        }
                        else {
                            sindex3 = 2;
                            target.check(R.id.radioButton8);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            //image retrieve
        stRef.child(mAuth.getCurrentUser().getUid()).getBytes(TEN_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            circleImageView.setImageBitmap(bmp);
        }).addOnFailureListener(e -> {

        });


        for(int i=0; i<5; i++){
            radioButtons[i].setEnabled(false);
        }

        gender.setEnabled(false);
        target.setEnabled(false);

        main_edit.setOnClickListener(v -> {
            main_edit.setVisibility(View.GONE);
            main_save.setVisibility(View.VISIBLE);
            main_cancel.setVisibility(View.VISIBLE);
            weight_edit.setVisibility(View.GONE);
            edit_btn.setVisibility(View.GONE);
            upload_btnl.setVisibility(View.VISIBLE);

            name.setEnabled(true);
            phone_number.setEnabled(true);


        });


        main_cancel.setOnClickListener(v -> {
            main_edit.setVisibility(View.VISIBLE);
            main_save.setVisibility(View.GONE);
            main_cancel.setVisibility(View.GONE);
            weight_edit.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.VISIBLE);
            upload_btnl.setVisibility(View.GONE);
            email.setText(semail);
            name.setText(sname);
            phone_number.setText(sphonenum);
            name.setEnabled(false);
            phone_number.setEnabled(false);
        });

        //Image selection from gallary
        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData()!= null){
                        Uri selectedImageUri = result.getData().getData();
                        circleImageView.setImageURI(selectedImageUri);
                        Drawable drawable = circleImageView.getDrawable();
                        bitmap = ((BitmapDrawable) drawable).getBitmap();
                    }
                    else{
                        Toast.makeText(myContext, "You haven't Selected any image!", Toast.LENGTH_LONG).show();
                    }
                }
        );

        upload_btnl.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            resultLauncher.launch(galleryIntent);
        });


        main_save.setOnClickListener(v -> {
            String s1 = name.getText().toString();
            if(s1.isEmpty()){
                name.setError("Required!");
                return;
            }
            String s2 = phone_number.getText().toString();
            if(s2.isEmpty()){
                phone_number.setError("Required!");
                return;
            }

            if(bitmap != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                if(data.length > TEN_MEGABYTE){
                    Toast.makeText(myContext, "Size is too large!", Toast.LENGTH_LONG).show();
                    return;
                }

                PopupLoading popupLoading = new PopupLoading(myContext);
                popupLoading.show();
                stRef.child(mAuth.getCurrentUser().getUid()).putBytes(data).addOnSuccessListener(taskSnapshot -> {
                    popupLoading.dismiss();
                    Toast.makeText(myContext, "Successfully Uploaded!", Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    popupLoading.dismiss();
                    PopupDialog popupDialog = new PopupDialog(myContext, "Error! "+e.getMessage());
                    popupDialog.show();
                });
            }

            Mainprofile mainprofile = new Mainprofile(s1, semail, s2);
            myRef.child("Mainprofile").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(mainprofile);
            Toast.makeText(myContext, "Successfully Updated Profile!", Toast.LENGTH_SHORT).show();

            name.setEnabled(false);
            phone_number.setEnabled(false);
            sname = s1;
            sphonenum = s2;
            upload_btnl.setVisibility(View.GONE);
            main_save.setVisibility(View.GONE);
            main_cancel.setVisibility(View.GONE);
            edit_btn.setVisibility(View.VISIBLE);
            weight_edit.setVisibility(View.VISIBLE);
            main_edit.setVisibility(View.VISIBLE);
        });


        weight_edit.setOnClickListener(v -> {
            main_edit.setVisibility(View.GONE);
            weight_edit.setVisibility(View.GONE);
            edit_btn.setVisibility(View.GONE);
            weight_save.setVisibility(View.VISIBLE);
            weight_cancel.setVisibility(View.VISIBLE);
            cweight.setEnabled(true);
            gweight.setEnabled(true);

        });

        weight_cancel.setOnClickListener(v -> {
            main_edit.setVisibility(View.VISIBLE);
            weight_edit.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.VISIBLE);
            weight_save.setVisibility(View.GONE);
            weight_cancel.setVisibility(View.GONE);
            cweight.setEnabled(false);
            gweight.setEnabled(false);
            Objects.requireNonNull(cweight.getEditText()).setText(cwei);
            Objects.requireNonNull(gweight.getEditText()).setText(pwei);
        });

        weight_save.setOnClickListener(v -> {
            String s1 = Objects.requireNonNull(cweight.getEditText()).getText().toString();
            if(s1.isEmpty()){
                cweight.setError("Required!");
                return;
            }
            float f1, f2;
            try {
                f1 = Float.parseFloat(s1);
            }catch (Exception e){
                cweight.setError("Enter Valid Weight!");
                return;
            }

            String s2 = Objects.requireNonNull(gweight.getEditText()).getText().toString();
            if(s2.isEmpty()){
                gweight.setError("Required!");
                return;
            }

            try {
                f2 = Float.parseFloat(s2);
            }catch (Exception e){
                gweight.setError("Enter Valid Weight!");
                return;
            }

            try {
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                final String TABLE_NAME = "WeightTable";
                SQLiteDatabase db = myContext.openOrCreateDatabase("Fitness", Context.MODE_PRIVATE, null);

                final String COMMAND = "SELECT COUNT(*) FROM "+ TABLE_NAME + " WHERE Day = "+ day + " AND Month = "+
                        month + " AND Year = "+year;
                Cursor cursor = db.rawQuery(COMMAND, null);
                cursor.moveToFirst();
                int icount = cursor.getInt(0);
                if(icount > 0){
                    String COMM = "UPDATE "+ TABLE_NAME + " SET Current_weight = "+ f1 + ", Goal_weight = "+ f2 +
                            " WHERE Day = "+ day + " AND Month = "+ month + " AND Year = "+year;
                    db.execSQL(COMM);
                }
                else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Current_weight", f1);
                    contentValues.put("Goal_weight", f2);
                    contentValues.put("Day", day);
                    contentValues.put("Month", month);
                    contentValues.put("Year", year);
                    db.insert(TABLE_NAME, null, contentValues);
                }
                cursor.close();
                db.close();

                myRef.child("AdditionalProfile").child(mAuth.getCurrentUser().getUid()).child("weight").setValue(f1);
                myRef.child("AdditionalProfile").child(mAuth.getCurrentUser().getUid()).child("goal_weight").setValue(f2);

                main_edit.setVisibility(View.VISIBLE);
                weight_edit.setVisibility(View.VISIBLE);
                edit_btn.setVisibility(View.VISIBLE);
                weight_save.setVisibility(View.GONE);
                weight_cancel.setVisibility(View.GONE);
                cweight.setEnabled(false);
                gweight.setEnabled(false);
                cwei = s1;
                pwei = s2;

                Toast.makeText(myContext, "Successfully Updated!", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(myContext, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });





        edit_btn.setOnClickListener(v -> {
            main_edit.setVisibility(View.GONE);
            weight_edit.setVisibility(View.GONE);
            edit_btn.setVisibility(View.GONE);
            save_btn.setVisibility(View.VISIBLE);
            cancel_btn.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            age.setEnabled(true);
            height.setEnabled(true);
            for(int i =0; i< 5; i++) radioButtons[i].setEnabled(true);
            gender.setEnabled(true);
            target.setEnabled(true);
            spinner1.setEnabled(true);
            spinner2.setEnabled(true);
        });


        gender.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.radioButton4){
                gender_val = 0;
            }
            else {
                gender_val = 1;
            }
        });


        target.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.radioButton6){
                spinner2.setVisibility(View.GONE);
                taget_index = 0;
            }

            else {
                spinner2.setVisibility(View.VISIBLE);
                if(checkedId == R.id.radioButton7){
                    taget_index = 1;
                }
                else {
                    taget_index = 2;
                }
            }
        });



        logout_btn.setOnClickListener(v -> {
            PopupLogout popupLogout = new PopupLogout(myContext);
            popupLogout.show();
        });


        cancel_btn.setOnClickListener(v -> {
            main_edit.setVisibility(View.VISIBLE);
            weight_edit.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.VISIBLE);
            save_btn.setVisibility(View.GONE);
            cancel_btn.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            age.setEnabled(false);
            height.setEnabled(false);
            for(int i =0; i< 5; i++) radioButtons[i].setEnabled(false);
            gender.setEnabled(false);
            target.setEnabled(false);
            spinner1.setEnabled(false);
            spinner2.setEnabled(false);
            spinner1.setSelectedIndex(sindex1);
            spinner2.setSelectedIndex(sindex2);
            Objects.requireNonNull(age.getEditText()).setText(sage);
            Objects.requireNonNull(height.getEditText()).setText(sheigh);

            if(sindex3 == 0){
                target.check(R.id.radioButton6);
            }
            else if(sindex3 == 1){
                target.check(R.id.radioButton7);
            }
            else {
                target.check(R.id.radioButton8);
            }

            if(gender_no == 0){
                gender.check(R.id.radioButton4);
            }
            else{
                gender.check(R.id.radioButton5);
            }

        });


        save_btn.setOnClickListener(v -> {
            String s1 = Objects.requireNonNull(age.getEditText()).getText().toString();
            if(s1.isEmpty()){
                age.setError("Required!");
                return;
            }
            int f1;
            float f2;
            try {
                f1 = Integer.parseInt(s1);
            }catch (Exception e){
                age.setError("Enter Valid age!");
                return;
            }

            String s2 = Objects.requireNonNull(height.getEditText()).getText().toString();
            if(s2.isEmpty()){
                height.setError("Required!");
                return;
            }

            try {
                f2 = Float.parseFloat(s2);
            }catch (Exception e){
                height.setError("Enter Valid height!");
                return;
            }
            String[] ghhh = new String[]{"Male", "Female"};
            DetailsProfile detailsProfile = new DetailsProfile(f1, taget_index, spinner2.getSelectedIndex(),
                    spinner1.getSelectedIndex(), ghhh[gender_val], f2, Float.parseFloat(cwei), Float.parseFloat(pwei));

            sage = s1;
            sheigh = s2;
            sindex3 = taget_index;
            gender_no = gender_val;
            sindex1 = spinner1.getSelectedIndex();
            sindex2 = spinner2.getSelectedIndex();

            myRef.child("AdditionalProfile").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).
                    setValue(detailsProfile);


            main_edit.setVisibility(View.VISIBLE);
            weight_edit.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.VISIBLE);
            save_btn.setVisibility(View.GONE);
            cancel_btn.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            age.setEnabled(false);
            height.setEnabled(false);
            for(int i =0; i< 5; i++) radioButtons[i].setEnabled(false);
            gender.setEnabled(false);
            target.setEnabled(false);
            spinner1.setEnabled(false);
            spinner2.setEnabled(false);

            Toast.makeText(myContext, "Successfully Updated!", Toast.LENGTH_SHORT).show();

        });


        restore_btn.setOnClickListener(v -> {

        });

        return view;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        myContext = context;
    }

    class PopupLogout extends Dialog{

        public PopupLogout(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.popup_logout);
            Button bt1, bt2;
            bt1 = findViewById(R.id.button10);
            bt2 = findViewById(R.id.button11);
            this.setCancelable(false);

            bt1.setOnClickListener(v -> {
                this.dismiss();
                ((CerterPage) requireActivity()).logoutOption();
            });

            bt2.setOnClickListener(v -> this.dismiss());
        }
    }
}
