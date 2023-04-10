package com.example.myzipzap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button scanner, generator;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();// initialize the firebase authentication method
        scanner = findViewById(R.id.scanBtn);
        generator = findViewById(R.id.genBtn);
        Toast.makeText(this, "ID: "+ currentUser.getUid(), Toast.LENGTH_SHORT).show();

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(MainActivity.this, QRScanner.class)));
            }
        });

        generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(MainActivity.this, QRGenerator.class)));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser ==null){
            startActivity((new Intent(MainActivity.this,LoginActivity.class)));
        }
    }


}
