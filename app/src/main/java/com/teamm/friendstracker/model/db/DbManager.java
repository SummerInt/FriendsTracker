package com.teamm.friendstracker.model.db;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teamm.friendstracker.model.entity.User;
import com.teamm.friendstracker.ui.FriendActivity;

import java.util.ArrayList;

public class DbManager {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private com.google.firebase.auth.FirebaseAuth.AuthStateListener mAuthListener;
    public static FirebaseUser users = mAuth.getInstance().getCurrentUser();
    public static User user = new User();
    public static User userFriend = new User();

    public static ArrayList<User> friends = new ArrayList<User>();

    public static void write(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FriendsTracker");
        users = mAuth.getCurrentUser();
        myRef.child("Users").child(users.getUid()).setValue(user);
    }

    public static void read(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        users = mAuth.getCurrentUser();
        final DatabaseReference myRef = database.getReference("FriendsTracker");
        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child("Users").child(users.getUid()).getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public static void read(final String userStr){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker");
        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userFriend = dataSnapshot.child("Users").child(userStr).getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public static void readFriends(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker").child("Users");
        ChildEventListener valueEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //read(dataSnapshot.getKey());
                friends.add(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void signOut(){
        mAuth.signOut();users = mAuth.getCurrentUser();
    }

    public static Uri getAvatarUri() {
        StorageReference storageReference = FirebaseStorage
                .getInstance()
                .getReference()
                .child(DbManager.users.getUid()).child("avatar.jpg");
        return Uri.parse(storageReference.getPath());
    }
    public static StorageReference getAvatarStorageReference() {
        return FirebaseStorage
                .getInstance()
                .getReference()
                .child(DbManager.users.getUid()).child("avatar.jpg");
    }
}