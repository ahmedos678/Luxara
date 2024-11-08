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

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private TextView forgot;
    private EditText email;
    private Button btnPass;
    private ProgressDialog progressDialog;
    private String emailS;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        forgot=findViewById(R.id.login_forgot);
        email=findViewById(R.id.email_forgot_password);
        btnPass=findViewById(R.id.btn_forgot_password);

        firebaseAuth =FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        btnPass.setOnClickListener(view -> {
            emailS=email.getText().toString().trim();

            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            firebaseAuth.sendPasswordResetEmail(emailS).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this,Login.class));
                    progressDialog.dismiss();
                    finish();
                }
                else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        });

        forgot.setOnClickListener(view -> {
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);
        });
    }

}