package com.example.myzipzap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button registerBtn;
    TextInputEditText registerEmailET,registerPasswordET;
    TextView loginHereTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        registerBtn = findViewById(R.id.registerBtn);
        registerEmailET = findViewById(R.id.registerEmailET);
        registerPasswordET = findViewById(R.id.registerPasswordET);
        loginHereTV = findViewById(R.id.loginHereTV);
        mAuth = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(view -> {
            createUser();
        });
        loginHereTV.setOnClickListener(view -> {
            startActivity(new Intent(Register.this, LoginActivity.class));
        });

    }

    private void createUser() {
        String email = registerEmailET.getText().toString();
        String password = registerPasswordET.getText().toString();
        if (TextUtils.isEmpty(email)) {
            registerEmailET.setError("Email cannot be empty");
            registerEmailET.requestFocus();

        } else if (TextUtils.isEmpty(password)) {
            registerPasswordET.setError("Password cannot be empty");
            registerPasswordET.requestFocus();

        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, LoginActivity.class));
                    } else {
                        Toast.makeText(Register.this, "registration error: " + (task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}