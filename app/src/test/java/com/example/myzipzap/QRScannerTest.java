package com.example.myzipzap;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.app.Person;
import android.content.Intent;
import android.view.View;

import java.net.DatagramPacket;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.PaymentData;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

public class QRScannerTest {
QRScanner qrScanner = mock(QRScanner.class);
int requestCode, resultCode;
Intent data;


    @Test
    public void testActivityResult() {
        doNothing().when(qrScanner).onActivityResult(requestCode, resultCode, data);
        qrScanner.onActivityResult(requestCode, resultCode, data);
        verify(qrScanner, times(1)).onActivityResult(requestCode, resultCode, data);
    }

    @Test
    public void retrieveDatabaseBalance() {
    }

    @Test
    public void setTextFields() {
    }

    @Test
    public void onClick() {
    }

    @Test
    public void onActivityResult() {
    }

    @Test
    public void updateBalance() {
    }
}