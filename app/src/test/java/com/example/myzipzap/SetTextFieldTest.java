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

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    @Test
    public void testSetTextFields2() {
        // create an instance of the class that contains this method
        QRScanner myClass = new QRScanner();
        long expectedBalance = 100L;
        String expectedUserId = "testUser";
        String expectedEmail = "testEmail@example.com";
        // set up any required data or objects
        myClass.dbCredit = expectedBalance;
        myClass.userId = expectedUserId;
        myClass.email = expectedEmail;
        myClass.balText = balText;
        myClass.idText = idText;
        myClass.emailText = emailText;
        // Call the method under test
        myClass.setTextFields();
        // call the method to be tested
        myClass.setTextFields();

        // assert that each statement has been executed
        assertEquals("Balance: €" + expectedBalance / 100, balText.getText());
        assertEquals(expectedUserId, idText.getText());
        assertEquals(expectedEmail, emailText.getText());
    }
    @Test
    public void testFirebaseDatabaseWrite() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        // Write a value to the database
        myRef.setValue("Hello, World!");
        myRef = database.getReference("message");
        // Read the value from the database and assert that it matches the expected value
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assertEquals("Hello, World!", value);
            }



            @Override
            public void onCancelled(DatabaseError error) {
                fail("Test was cancelled: " + error.getMessage());
            }
        });
    }
}
