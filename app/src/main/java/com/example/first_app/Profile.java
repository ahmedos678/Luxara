package com.example.first_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private EditText name, phone, cin, email;
    private Button btnLogOut, btnEdit;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference loggeduserReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);


        name = findViewById(R.id.name_profile);
        phone = findViewById(R.id.phone_profile);
        cin = findViewById(R.id.cin_profile);
        email = findViewById(R.id.email_profile);
        btnLogOut = findViewById(R.id.btn_logout_profile);
        btnEdit = findViewById(R.id.btn_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        loggeduserReference = firebaseDatabase.getReference().child("Users").child(firebaseUser.getUid());
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        loggeduserReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                name.setText(snapshotUser.child("fullName").getValue().toString());
                email.setText(snapshotUser.child("email").getValue().toString());
                phone.setText(snapshotUser.child("phone").getValue().toString());
                cin.setText(snapshotUser.child("cin").getValue().toString());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "" + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        btnLogOut.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences("checkBoxRemember", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean("rememberMe", false);
            editor.apply();
            firebaseAuth.signOut();

            startActivity(new Intent(this, FirstInterface.class));
            Toast.makeText(this, "Log Out successfull", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnEdit.setOnClickListener(view -> {
            String inputName=name.getText().toString().trim();
            String inputCin=cin.getText().toString().trim();
            String inputPhone=phone.getText().toString().trim();

            loggeduserReference.child("fullName").setValue(inputName);
            loggeduserReference.child("cin").setValue(inputCin);
            loggeduserReference.child("phone").setValue(inputPhone);

            Toast.makeText(this, "Your Data has been changed successfully", Toast.LENGTH_SHORT).show();
        });

    }
}