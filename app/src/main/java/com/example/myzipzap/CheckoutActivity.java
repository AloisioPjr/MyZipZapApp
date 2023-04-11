package com.example.myzipzap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myzipzap.databinding.CheckoutActivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class CheckoutActivity extends AppCompatActivity {
    private static final int LOAD_PAYMENT_REQUEST_CODE = 991;
    private static final long SHIPPING_COST_CENTS = 90 * PaymentInfo.CENTS_IN_A_UNIT.longValue();
    private PaymentsClient paymentsClient;
    View googlePayButton;
    private CheckoutActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_activity);

        paymentsClient = PaymentInfo.createPaymentsClient(this);
        possiblyShowGooglePayButton();
    }

    private void possiblyShowGooglePayButton() {
        final Optional<JSONObject> isReadyToPayJson = PaymentInfo.getIsReadyToPayRequest();
        if(!isReadyToPayJson.isPresent()) {
            return;
        }

        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(this,
                new OnCompleteListener<Boolean>() {
            @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    setGooglePayAvailable(task.getResult());
                }else {
                    Log.w("Not ready to pay", task.getException());
                }
            }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this, R.string.google_pay_status_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        final String paymentInfo = paymentData.toJson();
        if(paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show();

            Log.d("Google Pay Token: ", token);
        }catch (JSONException e) {
            throw new RuntimeException("Request cannot be parsed");
        }
    }

    private void requestPayment(View view) {
        googlePayButton.setClickable(false);


    }
}