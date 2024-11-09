package com.example.first_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstInterface extends AppCompatActivity {
    private Button log,sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_interface);

        log=findViewById(R.id.login_interface);
        sign=findViewById(R.id.signup_interface);

        SharedPreferences preferences = getSharedPreferences("checkBoxRemember", MODE_PRIVATE);
        boolean isChecked = preferences.getBoolean("rememberMe", false);

        if (isChecked) {
            startActivity(new Intent(this, Home.class));
        }

        log.setOnClickListener(view -> {
            Intent intent =new Intent(this,Login.class);
            startActivity(intent);
        });

        sign.setOnClickListener(view -> {
            Intent intent =new Intent(this,SignUp.class);
            startActivity(intent);
        });



    }
}