package com.example.myzipzap;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.firebase.database.DatabaseReference;
import java.util.HashMap;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
    public class UpdateDatabaseBalanceTest {
        @Mock
        private DatabaseReference databaseReference;
        @Mock
        private HashMap<String, Object> userHashmap;

        @Test
        public void testUpdateBalance() {
            // Set up mock values
            long expectedBalance = 100L;
            when(userHashmap.get(expectedBalance)).thenReturn(expectedBalance);
            // Set up the class under test
            QRScanner myClass = new QRScanner();
            myClass.databaseReference = databaseReference;
            // Call the method under test
            myClass.updateBalance();
            // Verify that the method updates the database with the correct HashMap object
            verify(databaseReference).child(myClass.userId);
            verify(databaseReference).updateChildren(userHashmap);
        }
    }