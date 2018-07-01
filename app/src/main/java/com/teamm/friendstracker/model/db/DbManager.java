package com.teamm.friendstracker.model.db;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamm.friendstracker.model.entity.User;

public class DbManager {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private com.google.firebase.auth.FirebaseAuth.AuthStateListener mAuthListener;
    static FirebaseUser users = mAuth.getInstance().getCurrentUser();
    public static User user;

    public static String key;

    public static void write(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child("users").child(users.getUid()).setValue(user);
    }

    public static void signOut(){
        mAuth.signOut();
    }

    public static void setKey(String key){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("keys");
        myRef.child("key").child(users.getUid()).setValue(key);
    }

    public static String getKey(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("keys");
        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                key = dataSnapshot.child("key").child(users.getUid()).getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return key;
    }
}
