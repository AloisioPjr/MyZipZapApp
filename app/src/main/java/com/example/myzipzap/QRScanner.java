package com.example.myzipzap;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.myzipzap.util.PaymentsUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

// implements onClickListener for the onclick behaviour of button
public class QRScanner extends AppCompatActivity implements View.OnClickListener {
    //FirebaseAuth mAuth;

    FloatingActionButton scanBtn;

    TextView messageText, balText, messageFormat, idText, emailText;

    DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;

    String vehicleID,email, userId;

    public static long topUp;

    long dbCredit, newValue;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.qr_code_icon);
        scanBtn = findViewById(R.id.scanBtn);
        vehicleID = "38e43b50-de39-11ed-b5ea-0242ac120002";
        messageText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textFormat);
        balText = findViewById(R.id.balContent);
        email = currentUser.getEmail();
        String userName = currentUser.getDisplayName();
        idText = findViewById(R.id.idContent);
        emailText = findViewById(R.id.textContent);
        scanBtn.setOnClickListener(this);
        userId = currentUser.getUid();

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch(item.getItemId()) {

                        case R.id.qr_code_icon:

                            return true;

                        case R.id.top_up_icon:
                            startActivity(new Intent(getApplicationContext(), GooglePay.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                            return true;
                        case R.id.bus_icon:
                            startActivity(new Intent(getApplicationContext(), BusTimeActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                            return true;

                        case R.id.settings_icon:
                            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                            return true;


                }
                return false;
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference(userId).child("User Balance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbCredit = snapshot.getValue(long.class);
                //dbCredit = credit +topUp;
                balText.setText("Balance: €" + dbCredit/100 );
                idText.setText(userId);
                emailText.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle the error
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan the QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                String content = intentResult.getContents();
                if (content.contentEquals(vehicleID)) {

                    if (dbCredit >= 200.00) {
                        // increment the value
                        newValue = (long) (dbCredit - 200.00);
                        balText.setText("Balance: €" + String.valueOf(dbCredit / 100) + "\nNew Balance: €" + (newValue / 100));
                        HashMap userHashmap = new HashMap();
                        userHashmap.put("User Balance",newValue);
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child(userId).updateChildren(userHashmap);
                    } else {
                        balText.setText("Not enough funds for this \ntransaction. Please top up. ");
                    }


                }else{
                    Toast.makeText(getBaseContext(), "Please scan a valid QR code", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
