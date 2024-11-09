package com.example.first_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextView sign, forgot;
    private EditText email, pass;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private String emailS, passS;
    private FirebaseAuth firebaseAuth;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        forgot = findViewById(R.id.forgot_login);
        sign = findViewById(R.id.signup_login);
        email = findViewById(R.id.email_login);
        pass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        rememberMe = findViewById(R.id.remember_login);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        sign.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        });

        forgot.setOnClickListener(view -> {
            Intent intent = new Intent(this, ForgotPassword.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            emailS = email.getText().toString().trim();
            passS = pass.getText().toString().trim();

            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(emailS, passS).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    CheckEmailVerification();
                } else {
                    Toast.makeText(this, "Wrong Info", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        });

    }

    private void CheckEmailVerification() {
        FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        if (loggedUser != null) {
            if (loggedUser.isEmailVerified()) {
                checkRememberMe();
                startActivity(new Intent(this, Home.class));
                progressDialog.dismiss();
                finish();
            } else {
                Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                progressDialog.dismiss();
            }

        }
    }


    private void checkRememberMe() {
        SharedPreferences preferences = getSharedPreferences("checkBoxRemember", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (rememberMe.isChecked()) {
            editor.putBoolean("rememberMe", true);
            editor.apply();
        } else {
            editor.putBoolean("rememberMe", false);
            editor.apply();
        }

    }
}