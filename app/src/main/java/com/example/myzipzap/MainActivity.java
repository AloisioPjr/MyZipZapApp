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

import com.example.myzipzap.databinding.ActivityMainBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    SettingsFragment settingsFragment = new SettingsFragment();
    BusTimeFragment busTimeFragment = new BusTimeFragment();
    TopUpFragment topUpFragment = new TopUpFragment();
    ScannerFragment scannerFragment = new ScannerFragment();
    private FirebaseAuth mAuth;
    Button scanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();// initialize the firebase authentication method
        startActivity((new Intent(MainActivity.this, QRScanner.class)));

        // = findViewById(R.id.scanBtn);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, scannerFragment).commit();
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.setting_icon);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(8);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.qr_code_icon:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, scannerFragment).commit();
                        return true;

                    case R.id.top_up_icon:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, topUpFragment).commit();
                        return true;

                    case R.id.bus_icon:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, busTimeFragment).commit();
                        return true;

                    case R.id.setting_icon:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, settingsFragment).commit();
                        return true;


                }
                return false;
            }
        });
      /*  scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(MainActivity.this, QRScanner.class)));
            }
        });*/
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
