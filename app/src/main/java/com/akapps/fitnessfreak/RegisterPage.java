package com.akapps.fitnessfreak;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterPage extends AppCompatActivity {
    TextInputLayout name, email, phone_number, password, confirmpassword;
    TextView back_text;
    Button submit_button;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        back_text = findViewById(R.id.textView11);
        name = findViewById(R.id.outlinedTextField2);
        email = findViewById(R.id.outlinedTextField3);
        phone_number = findViewById(R.id.outlinedTextField4);
        password = findViewById(R.id.outlinedTextField5);
        confirmpassword = findViewById(R.id.outlinedTextField6);

        submit_button = findViewById(R.id.button2);

        submit_button.setOnClickListener(v -> {
            boolean isFalse = false;
            String s1 = Objects.requireNonNull(name.getEditText()).getText().toString();
            if(s1.isEmpty()){
                isFalse = true;
                name.setError("Name is Required!");
            }

            String s2 = Objects.requireNonNull(email.getEditText()).getText().toString();
            if(s2.isEmpty()){
                isFalse = true;
                email.setError("Name is Required!");
            }

            String s3 = Objects.requireNonNull(phone_number.getEditText()).getText().toString();
            if(s3.isEmpty()){
                isFalse = true;
                phone_number.setError("Required!");
            }
            String s4 = Objects.requireNonNull(password.getEditText()).getText().toString();
            if(s4.isEmpty()){
                isFalse = true;
                password.setError("Password is Required!");
            }
            String s5 = Objects.requireNonNull(confirmpassword.getEditText()).getText().toString();
            if(s5.isEmpty()){
                isFalse = true;
                confirmpassword.setError("Required!");
            }

            if(s4.length() < 6){
                isFalse = true;
                password.setError("Password too Short!");
            }

            if(!s4.equals(s5)){
                isFalse = true;
                confirmpassword.setError("Password Not Matched!");
            }
            if(isFalse) return;
            PopupLoading popupLoading = new PopupLoading(RegisterPage.this);
            popupLoading.show();

            mAuth.createUserWithEmailAndPassword(s2, s4).addOnSuccessListener(authResult -> {
                Mainprofile mainprofile = new Mainprofile(s1, s2, s3);
                myRef.child("Mainprofile")
                        .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(mainprofile);

                mAuth.getCurrentUser().sendEmailVerification();
                String str = "A Verification Mail Had been Sent To your Email Address. Please verify it & then login.";
                popupLoading.dismiss();
                PopupDialog popupDialog = new PopupDialog(RegisterPage.this, str);
                popupDialog.show();
                popupDialog.setOnDismissListener(dialog -> finish());

            }).addOnFailureListener(e -> {
                String str = "Error! "+ e.getMessage();
                popupLoading.dismiss();
                PopupDialog popupDialog = new PopupDialog(RegisterPage.this, str);
                popupDialog.show();
            });

        });


        back_text.setOnClickListener(v -> back_pressed());

        //info_txt.setOnClickListener(LoginPage::onClick);
    }

    @Override
    public void onBackPressed() {
        back_pressed();
    }

    void back_pressed()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterPage.this)
                .setMessage("All progress will reset, are you sure to leave this Page?")
                .setPositiveButton("Yes", (dialog, which) -> RegisterPage.this.finish())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create();

        alertDialog.show();
    }

}