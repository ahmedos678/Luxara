package com.example.first_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.first_app.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private TextView log;
    private EditText name, email, phone, cin, pass, confirmPass;
    private Button btnSignUp;
    private String nameS, emailS, phoneS, cinS, passS, confirmPassS;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        log = findViewById(R.id.login_signup);
        name = findViewById(R.id.fullname_signup);
        email = findViewById(R.id.email_signup);
        phone = findViewById(R.id.phone_signup);
        cin = findViewById(R.id.cin_signup);
        pass = findViewById(R.id.password_signup);
        confirmPass = findViewById(R.id.confirm_pass_signup);
        btnSignUp = findViewById(R.id.btn_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btnSignUp.setOnClickListener(view -> {
            if (validate()) {
                progressDialog.setMessage("please wait...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(emailS, passS).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendEmailVerification();
                    } else {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });


            }
        });

        log.setOnClickListener(view -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });


    }

    private void sendEmailVerification() {
        FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        if (loggedUser != null) {
            loggedUser.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    sendUserData();
                    Toast.makeText(this, "Registration done,Check your email", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(this, Login.class));
                    finish();
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

        User user = new User(nameS, emailS, cinS, phoneS);
        databaseReference.child("" + firebaseAuth.getUid()).setValue(user);

    }


    private boolean validate() {
        boolean result = false;
        nameS = name.getText().toString().trim();
        emailS = email.getText().toString().trim();
        phoneS = phone.getText().toString().trim();
        cinS = cin.getText().toString().trim();
        passS = pass.getText().toString().trim();
        confirmPassS = confirmPass.getText().toString().trim();

        if (nameS.length() < 7) {
            name.setError("invalid name");
        } else if (!isValidString(emailS, EMAIL_REGEX)) {
            email.setError("invalid email");
        } else if (phoneS.length() != 8) {
            phone.setError("invalid email");
        } else if (cinS.length() != 8) {
            cin.setError("invalid email");
        } else if (passS.length() < 5) {
            pass.setError("invalid pass");
        } else if (!confirmPassS.equals(passS)) {
            confirmPass.setError("invalid pass");
        } else result = true;

        return result;
    }

    private boolean isValidString(String email, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}