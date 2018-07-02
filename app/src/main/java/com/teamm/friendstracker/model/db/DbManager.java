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
    public static FirebaseUser users = mAuth.getInstance().getCurrentUser();
    public static User user = new User();

    public static void write(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FriendsTracker");
        myRef.child("Users").child(users.getUid()).child("User").setValue(user);
    }

    public static void read(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker");
        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child("Users").child(users.getUid()).child("User").getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public static void signOut(){
        mAuth.signOut();
    }
}
