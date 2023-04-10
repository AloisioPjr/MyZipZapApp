package com.example.myzipzap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

// implements onClickListener for the onclick behaviour of button
public class QRScanner extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn;
    TextView messageText, balText, messageFormat, idText, emailText;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.scanBtn);
        messageText = findViewById(R.id.textContent);
        messageFormat = findViewById(R.id.textFormat);
        balText = findViewById(R.id.balContent);
        idText = findViewById(R.id.idContent);
        emailText = findViewById(R.id.textContent);
        Toast.makeText(this, ""+ currentUser.getUid(), Toast.LENGTH_SHORT).show();

        // adding listener to the button
        scanBtn.setOnClickListener(this);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Could not fetch QR data", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                String userId = currentUser.getUid();
                String content = intentResult.getContents();
                String email = currentUser.getEmail();
                double balance = 200.00;
                databaseReference = FirebaseDatabase.getInstance().getReference();
                //databaseReference.setValue(intentResult.getContents());
                databaseReference.child("User ID").push().setValue(userId);
                databaseReference.child("Email").push().setValue(email);
                databaseReference.child("Balance").push().setValue(balance);

                idText.setText(userId);
                emailText.setText(email);

                if (balance != 0) {
                    // increment the value
                    Double newValue = balance - 2.00;
                    balText.setText("Balance: "+ String.valueOf(balance)+ "\nNew Balance: "+ newValue);
                    databaseReference.child("Balance").setValue(newValue);
                }
                messageFormat.setText(intentResult.getFormatName());


                /*databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double currentValue = snapshot.getValue(Double.class);
                        if (currentValue != null) {
                            // increment the value
                            Double newValue = currentValue - 2.00;
                            // update the value in the database
                            balText.setText(content+"\nNew Balance: "+newValue);
                            databaseReference.child("New Balance").push().setValue(newValue);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // handle the error
                        Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });*/

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject() {{
            put("type", "PAYMENT_GATEWAY");
            put("parameter", new JSONObject() {{
                put("gateway", "example");
                put("gatewayMerchantId", "exampleGatewayMerchantId");
            }});
        }};
    }

}
