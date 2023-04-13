package com.example.myzipzap;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOUserDetails {
    private static DatabaseReference databaseReference;
    public DAOUserDetails(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(ReadWriteUserDetails.class.getSimpleName());
    }
    public static Task<Void> add(ReadWriteUserDetails readWriteUserDetails){
            return databaseReference.push().setValue(readWriteUserDetails);
    }
    public static Task<Void>update(String UID, HashMap<String,Object> hashMap){
        return databaseReference.child(UID).updateChildren(hashMap);
    }
}
