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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button registerBtn;
    TextInputEditText registerEmailET,registerPasswordET;
    TextView loginHereTV;
    ProgressBar registerProgressBar;
    private String userBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        registerBtn = findViewById(R.id.registerBtn);
        registerEmailET = findViewById(R.id.registerEmailET);
        registerPasswordET = findViewById(R.id.registerPasswordET);
        loginHereTV = findViewById(R.id.loginHereTV);
        registerProgressBar = findViewById(R.id.registerProgressBar);

        //



        userBalance = "0.00";
        registerBtn.setOnClickListener(view -> {
            createUser();
        });
        loginHereTV.setOnClickListener(view -> {
            startActivity(new Intent(Register.this, LoginActivity.class));
        });

    }

    private void createUser() {
        registerProgressBar.setVisibility(View.VISIBLE);
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
                    registerProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        //create the data in the realtime database

                        //extracting user reference from Realtime database
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("userBalance");
                        //save the details from user in the object writeUserDetails

                        referenceProfile.child(firebaseUser.getUid()).setValue(userBalance).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override// this on complete will only save the data if the first onComplete has been successful
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    firebaseUser.sendEmailVerification();
                                    Toast.makeText(Register.this, "An email confirmation has been sent, please check your email", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this, LoginActivity.class));
                                }else{
                                    Toast.makeText(Register.this, "registration error: " + (task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    } else {
                        Toast.makeText(Register.this, "registration error: " + (task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}