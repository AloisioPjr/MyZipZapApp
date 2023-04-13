package com.example.myzipzap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.UUID;

// implements onClickListener for the onclick behaviour of button
public class QRScanner extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    Button scanBtn;
    TextView messageText, balText, messageFormat;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;
    private String userId;
    private String userCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        // referencing and initializing
        // the button and textviews
        mAuth = FirebaseAuth.getInstance();
        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textFormat);
        balText = findViewById(R.id.balContent);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.qr_code_icon);


        balText.setText((CharSequence) databaseReference);
        userCredit = "300000.00";

        databaseReference= FirebaseDatabase.getInstance().getReference().child("userCredit");

        HashMap userHashmap = new HashMap();
        userHashmap.put("userCredit"+(FirebaseAuth.getInstance().getCurrentUser().getUid()),userCredit);

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(userHashmap);
        // adding listener to the button
        scanBtn.setOnClickListener(this);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()) {

                    case R.id.qr_code_icon:

                        return true;

                    case R.id.top_up_icon:
                        startActivity(new Intent(getApplicationContext(), TopUpActivity.class));
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
    }

    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }
    //protected void onActivityResult(int userCredit)
    @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                //String userId = UUID.randomUUID().toString();
                String content = intentResult.getContents();
                //String name = "Juan";
                databaseReference = FirebaseDatabase.getInstance().getReference();
                //databaseReference.setValue(intentResult.getContents());
                //databaseReference.child("Name").push().setValue(name);
                databaseReference.child("Balance").push().setValue(content);


                balText.setText(intentResult.getContents());
                messageFormat.setText(intentResult.getFormatName());


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
