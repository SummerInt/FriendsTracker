package com.teamm.friendstracker.model.db;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamm.friendstracker.model.entity.User;

public class DbManager {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private com.google.firebase.auth.FirebaseAuth.AuthStateListener mAuthListener;
    static FirebaseUser users = mAuth.getInstance().getCurrentUser();
    public static User user;

    public static void write(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child("users").child(users.getUid()).setValue(user);
    }

    public static void signOut(){
        mAuth.signOut();
    }

    public static void setKey(String key){

    }

    public static String getKey(){
        String key="";
        return key;
    }
}
