package com.example.myzipzap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myzipzap.databinding.ActivityGooglePayBinding;
import com.example.myzipzap.viewmodel.CheckoutViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

/**
 * Checkout implementation for the app
 */
public class GooglePay extends AppCompatActivity {

  // Arbitrarily-picked constant integer you define to track a request for payment data activity.
  private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

  private CheckoutViewModel model;

  private ActivityGooglePayBinding layoutBinding;
  private View googlePayButton;
  long dummyPriceCents;
  RadioGroup radGroup;
  RadioButton radBtn5, radBtn10, radBtn15, radBtn20;
  DatabaseReference databaseReference;
  BottomNavigationView bottomNavigationView;
  FirebaseUser currentUser;
  private String userId;
  long dbCredit;


  /**
   * Initialize the Google Pay API on creation of the activity
   *
   * @see AppCompatActivity#onCreate(android.os.Bundle)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeUi();
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    userId = currentUser.getUid();
    model = new ViewModelProvider(this).get(CheckoutViewModel.class);
    model.canUseGooglePay.observe(this, this::setGooglePayAvailable);
    dummyPriceCents = QRScanner.topUp;
    radGroup = findViewById(R.id.radioGroup);
    radBtn5 = findViewById(R.id.rad5);
    radBtn10 = findViewById(R.id.rad10);
    radBtn15 = findViewById(R.id.rad15);
    radBtn20 = findViewById(R.id.rad20);
    bottomNavigationView = findViewById(R.id.bottomNavigationView);
    bottomNavigationView.setSelectedItemId(R.id.top_up_icon);
    bottomNavigation();
    retrieveDatabaseBalance();
  }
  public void retrieveDatabaseBalance() {
    databaseReference = FirebaseDatabase.getInstance().getReference(userId).child("User Balance");
    databaseReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        dbCredit = snapshot.getValue(long.class);

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        // handle the error
        Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
      }
    });
  }
  public void bottomNavigation() {
    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

          case R.id.qr_code_icon:
            startActivity(new Intent(getApplicationContext(), QRScanner.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
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
  }
  private void initializeUi() {

    // Use view binding to access the UI elements
    layoutBinding = ActivityGooglePayBinding.inflate(getLayoutInflater());
     setContentView(layoutBinding.getRoot());

    // The Google Pay button is a layout file – take the root view
    googlePayButton = layoutBinding.googlePayButton.getRoot();
    googlePayButton.setOnClickListener(this::requestPayment);
  }

  /**
   * If isReadyToPay returned {@code true}, show the button and hide the "checking" text.
   * Otherwise, notify the user that Google Pay is not available. Please adjust to fit in with
   * your current user flow. You are not required to explicitly let the user know if isReadyToPay
   * returns {@code false}.
   *
   * @param available isReadyToPay API response.
   */
  private void setGooglePayAvailable(boolean available) {
    if (available) {
      googlePayButton.setVisibility(View.VISIBLE);
    } else {
      Toast.makeText(this, R.string.googlepay_status_unavailable, Toast.LENGTH_LONG).show();
    }
  }

  public void requestPayment(View view) {

    // Disables the button to prevent multiple clicks.
    googlePayButton.setClickable(false);

    // The price provided to the API should include taxes and shipping.
    // This price is not displayed to the user.
    if(radBtn5.isChecked()) {
      dummyPriceCents = 500;
    } else if(radBtn10.isChecked()) {
      dummyPriceCents = 1000;
    } else if(radBtn15.isChecked()) {
      dummyPriceCents = 1500;
    } else if(radBtn20.isChecked()) {
      dummyPriceCents = 2000;
    }

    long totalPriceCents = dummyPriceCents;
    final Task<PaymentData> task = model.getLoadPaymentDataTask(totalPriceCents);

    // Shows the payment sheet and forwards the result to the onActivityResult method.
    AutoResolveHelper.resolveTask(task, this, LOAD_PAYMENT_DATA_REQUEST_CODE);
  }

  /**
   * Handle a resolved activity from the Google Pay payment sheet.
   *
   * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
   * @param resultCode  Result code returned by the Google Pay API.
   * @param data        Intent from the Google Pay API containing payment or error data.
   * @see <a href="https://developer.android.com/training/basics/intents/result">Getting a result
   * from an Activity</a>
   */
  @SuppressWarnings("deprecation")
  // Suppressing deprecation until `registerForActivityResult` can be used with the Google Pay API.
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      // value passed in AutoResolveHelper
      case LOAD_PAYMENT_DATA_REQUEST_CODE:
        switch (resultCode) {

          case AppCompatActivity.RESULT_OK:
            PaymentData paymentData = PaymentData.getFromIntent(data);
            handlePaymentSuccess(paymentData);
            break;

          case AppCompatActivity.RESULT_CANCELED:
            // The user cancelled the payment attempt
            break;

          case AutoResolveHelper.RESULT_ERROR:
            Status status = AutoResolveHelper.getStatusFromIntent(data);
            handleError(status);
            break;
        }

        // Re-enables the Google Pay payment button.
        googlePayButton.setClickable(true);
    }
  }

  /**
   * PaymentData response object contains the payment information, as well as any additional
   * requested information, such as billing and shipping address.
   *
   * @param paymentData A response object returned by Google after a payer approves payment.
   * @see <a href="https://developers.google.com/pay/api/android/reference/
   * object#PaymentData">PaymentData</a>
   */
  private void handlePaymentSuccess(@Nullable PaymentData paymentData) {
    final String paymentInfo = paymentData.toJson();

    try {
      JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
      // If the gateway is set to "example", no payment information is returned - instead, the
      // token will only consist of "examplePaymentMethodToken".

      final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
      final String token = tokenizationData.getString("token");
      final JSONObject info = paymentMethodData.getJSONObject("info");
      final String billingName = info.getJSONObject("billingAddress").getString("name");


      ///////////////////////////////////
      Toast.makeText(
              this, getString(R.string.payments_show_name, billingName),
              Toast.LENGTH_LONG).show();
      updateBalance();

      // Logging token string.
      Log.d("Google Pay token: ", token);

    } catch (JSONException e) {
      throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
    }
  }
  public void updateBalance(){
    HashMap userHashmap = new HashMap();
    userHashmap.put("User Balance", dummyPriceCents + dbCredit);
    databaseReference = FirebaseDatabase.getInstance().getReference();
    databaseReference.child(userId).updateChildren(userHashmap);
  }
  /**
   * At this stage, the user has already seen a popup informing them an error occurred. Normally,
   * only logging is required.
   *
   * @param status will hold the value of any constant from CommonStatusCode or one of the
   *                   WalletConstants.ERROR_CODE_* constants.
   * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/wallet/
   * WalletConstants#constant-summary">Wallet Constants Library</a>
   */
  private void handleError(@Nullable Status status) {
    String errorString = "Unknown error.";
    if (status != null) {
      int statusCode = status.getStatusCode();
      errorString = String.format(Locale.getDefault(), "Error code: %d", statusCode);
    }

    Log.e("loadPaymentData failed", errorString);
  }
}