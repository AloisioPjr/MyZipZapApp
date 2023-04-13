package com.example.myzipzap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    Button scanner, generator;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();// initialize the firebase authentication method
        startActivity((new Intent(MainActivity.this, QRScanner.class)));

        //scanner = findViewById(R.id.scanBtn);
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
