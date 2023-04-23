package com.example.myzipzap;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import android.widget.TextView;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class SetTextFieldTest {
    @Mock
    private TextView balText;
    @Mock
    private TextView idText;
    @Mock
    private TextView emailText;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetTextFields() {
        // Set up mock values
        long expectedBalance = 100L;
        String expectedUserId = "testUser";
        String expectedEmail = "testEmail@example.com";
        // Set up the class under test
        QRScanner myClass = new QRScanner();
        myClass.dbCredit = expectedBalance;
        myClass.userId = expectedUserId;
        myClass.email = expectedEmail;
        myClass.balText = balText;
        myClass.idText = idText;
        myClass.emailText = emailText;
        // Call the method under test
        myClass.setTextFields();
        // Verify that the method sets the TextViews with the expected values
        verify(balText).setText("Balance: €" + expectedBalance / 100);
        verify(idText).setText(expectedUserId);
        verify(emailText).setText(expectedEmail);
    }
}
