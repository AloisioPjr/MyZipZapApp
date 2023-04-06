package com.example.myzipzap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button forgotPasswordBtn;
    Button loginBtn;
    TextView registerHereTV;
    TextInputEditText loginEmailET,loginPasswordET;
        ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //forgotPasswordBtn = (Button) findViewById(R.id.forgotPasswordBtn);
        loginBtn =(Button) findViewById(R.id.loginBtn);
        registerHereTV = findViewById(R.id.registerHereTV);
        loginEmailET = findViewById(R.id.loginEmailET);
        loginPasswordET = findViewById(R.id.loginPasswordET);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        mAuth = FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(view ->{
            loginUser();
        });
        registerHereTV.setOnClickListener(view ->{
            startActivity( new Intent(LoginActivity.this, Register.class));
        });
    }
    private void loginUser() {
        loginProgressBar.setVisibility(View.VISIBLE);
        String email = loginEmailET.getText().toString();
        String password = loginPasswordET.getText().toString();
        if (TextUtils.isEmpty(email)){
            loginEmailET.setError("Email cannot be empty");
            loginEmailET.requestFocus();

        }else if(TextUtils.isEmpty(password)){
            loginPasswordET.setError("Password cannot be empty");
            loginPasswordET.requestFocus();

        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    loginProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        Toast.makeText(LoginActivity.this, "Login error error: " + (task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}