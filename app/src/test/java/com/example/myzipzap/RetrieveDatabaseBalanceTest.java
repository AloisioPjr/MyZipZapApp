package com.example.myzipzap;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class RetrieveDatabaseBalanceTest {
    @Mock
    private DatabaseReference databaseReference;
    @Mock
    private DataSnapshot dataSnapshot;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveDatabaseBalance() {
        // Set up mock values
        long expectedBalance = 100L;
        when(dataSnapshot.getValue(long.class)).thenReturn(expectedBalance);
        // Set up the class under test
        QRScanner myClass = new QRScanner();
        myClass.databaseReference = databaseReference;
        // Call the method under test
        myClass.retrieveDatabaseBalance();
        // Verify that the method sets dbCredit correctly
        verify(databaseReference).addValueEventListener(any(ValueEventListener.class));
        verify(dataSnapshot).getValue(long.class);
        assertEquals(expectedBalance, myClass.dbCredit);
    }


}