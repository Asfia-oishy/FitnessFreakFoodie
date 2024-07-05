package com.akapps.fitnessfreak;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.securepreferences.SecurePreferences;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {
    TextView back_txt, info_txt, register_text, forgot_pass;
    EditText ed1, ed2;
    Button login_button;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences; //autologin er jnno uid save hcce ekhane
    boolean isPreviousLoginSaved = false;
    boolean passwordmarker = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        login_button = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = new SecurePreferences(this);
        info_txt = findViewById(R.id.textView2);
        back_txt = findViewById(R.id.textView3);
        ed1 = findViewById(R.id.email);
        ed2 = findViewById(R.id.password);
        forgot_pass = findViewById(R.id.textView6);
        register_text = findViewById(R.id.textView8);
        isPreviousLoginSaved = sharedPreferences.getBoolean("prevlogin", false);
//pass save er dialog er jonno
        if(mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified())
        {
            if(isPreviousLoginSaved){
                startActivity(new Intent(LoginPage.this, CerterPage.class));
                finish();
            }
            else
            {
                Autologin autologin = new Autologin(LoginPage.this);
                autologin.show();
            }
        }
        ed2.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (ed2.getRight() - ed2.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if(passwordmarker){
                        ed2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.ic_baseline_visibility_24, 0);
                        ed2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ed2.setSelection(ed2.getText().length());
                        passwordmarker = false;
                    }else {
                        ed2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                        ed2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        ed2.setSelection(ed2.getText().length());
                        passwordmarker = true;
                    }
                    return true;
                }
            }
            return false;
        });
        register_text.setOnClickListener(v -> startActivity(new Intent(LoginPage.this, RegisterPage.class)));


        back_txt.setOnClickListener(v -> {
            back_pressed();
        });

        forgot_pass.setOnClickListener(v -> {
            Passwordreset passwordreset = new Passwordreset(LoginPage.this);
            passwordreset.show();
        });

        login_button.setOnClickListener(v -> {
            String s1 = ed1.getText().toString();
            if(s1.isEmpty()){
                ed1.setError("Email is Required!");
                return;
            }
            String s2 = ed2.getText().toString();
            if(s2.isEmpty()){
                ed2.setError("Password is Required!");
                return;
            }

            PopupLoading popupLoading = new PopupLoading(LoginPage.this);
            popupLoading.show();
            mAuth.signInWithEmailAndPassword(s1, s2).addOnSuccessListener(authResult -> {
                if(!mAuth.getCurrentUser().isEmailVerified()){
                    popupLoading.dismiss();
                    EmailVerification emailVerification = new EmailVerification(LoginPage.this);
                    emailVerification.show();
                    return;
                }
                startActivity(new Intent(LoginPage.this, CerterPage.class));
                finish();
            }).addOnFailureListener(e -> {
                popupLoading.dismiss();
                String str = "Error! "+e.getMessage();
                PopupDialog popupDialog = new PopupDialog(LoginPage.this, str);
                popupDialog.show();
            });
        });



        //info_txt.setOnClickListener(LoginPage::onClick);
        info_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, UserManual.class));

            }
        });

    }

    @Override
    public void onBackPressed() {
        back_pressed();
    }

    void back_pressed()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginPage.this)
                .setMessage("Action will exit the app, sure to continue?")
                .setPositiveButton("Yes", (dialog, which) -> LoginPage.this.finish())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create();

        alertDialog.show();
    }

//forgotpass dile eei kaj hobe
    class Passwordreset extends Dialog{
        Button send;
        EditText ed;
        TextView cancel;
        public Passwordreset(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.popup_resetmail);
            send = findViewById(R.id.button3);
            ed = findViewById(R.id.email);
            cancel = findViewById(R.id.textView29);
            cancel.setOnClickListener(v -> this.dismiss());

            this.setCancelable(false);

            send.setOnClickListener(v -> {
                String mail = ed.getText().toString();
                if(mail.isEmpty()){
                    ed.setError("Required!");
                    return;
                }
                this.dismiss();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(unused -> {
                    String str = "Email had been sent to your email. Please Check your inbox. ";
                    PopupDialog popupDialog = new PopupDialog(LoginPage.this, str);
                    popupDialog.show();

                }).addOnFailureListener(e -> {
                    String str = "Error! "+ e.getMessage();
                    PopupDialog popupDialog = new PopupDialog(LoginPage.this, str);
                    popupDialog.show();
                });

            });
        }
    }

    class EmailVerification extends Dialog{

        public EmailVerification(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.popup_resendvmail);
            Button bt = findViewById(R.id.button5);
            TextView txt = findViewById(R.id.textView29);
            this.setCancelable(false);
            txt.setOnClickListener(v -> this.dismiss());


            bt.setOnClickListener(v -> {
                this.dismiss();
                if(mAuth.getCurrentUser() == null) return;
                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(unused -> {
                    String str = "Email sent successfully, please check your inbox Now!";
                    PopupDialog popupDialog = new PopupDialog(LoginPage.this, str);
                    popupDialog.show();
                }).addOnFailureListener(e -> {
                    String str = "Error! "+ e.getMessage();
                    PopupDialog popupDialog = new PopupDialog(LoginPage.this, str);
                    popupDialog.show();
                });
            });

        }
    }


    class Autologin extends Dialog {

        public Autologin(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.popup_autologin);
            TextView tx1, tx2;
            CheckBox checkBox;
            Button bt;
            tx1 = findViewById(R.id.textView29);
            tx2 = findViewById(R.id.textr);
            bt = findViewById(R.id.button16);
            this.setCancelable(false);
            tx1.setOnClickListener(v -> this.dismiss());
            checkBox = findViewById(R.id.checkBox);
            tx2.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
            bt.setOnClickListener(v -> {
                sharedPreferences.edit().putBoolean("prevlogin", checkBox.isChecked()).apply();
                this.dismiss();
                Toast.makeText(LoginPage.this, "Sign in successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginPage.this, CerterPage.class));
                LoginPage.this.finish();
            });
        }
    }
}