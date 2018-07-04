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
import com.teamm.friendstracker.model.entity.Coordinats;
import com.teamm.friendstracker.model.entity.User;
import com.teamm.friendstracker.ui.FriendActivity;
import com.teamm.friendstracker.ui.FriendSearchActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DbManager {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private com.google.firebase.auth.FirebaseAuth.AuthStateListener mAuthListener;
    public static FirebaseUser users = mAuth.getInstance().getCurrentUser();
    public static User user = new User();
    public static User userFriend = new User();
    public static String id;

    public static ArrayList<User> friends = new ArrayList<User>();

    public static ArrayList<String> friendsId = new ArrayList<String>();

    public static ArrayList<Coordinats> coordinats = new ArrayList<Coordinats>();

    public static ArrayList<User> serchFriends = new ArrayList<User>();

    public static HashMap<String,String> idEmailUsers = new HashMap<>();

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

    public static void readFriendId(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker").child("Friends").child(users.getUid());
        ChildEventListener valueEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //friendsId.add(dataSnapshot.getValue(String.class));
                friendsId.add(dataSnapshot.getKey());

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

    public static void readFriends(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker").child("Users");
        ChildEventListener valueEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                userFriend = dataSnapshot.getValue(User.class);
                if(friendsId.indexOf(dataSnapshot.getKey())!=-1) {
                    friends.add(userFriend);
                    FriendActivity.loadFriends();
                }
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
        mAuth.signOut();
    }

    public static void saveCoordinats(double latitude, double longitude){
        Coordinats coordinats = new Coordinats(latitude,longitude);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FriendsTracker");
        myRef.child("Coordinats").child(users.getUid()).setValue(coordinats);
    }

    public static void readCoordinats(String idUser){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker").child("Coordinats").child(idUser);
        ChildEventListener valueEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                coordinats.add(dataSnapshot.getValue(Coordinats.class));
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

    public static void serchFriend(final String email){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker").child("Users");
        ChildEventListener valueEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                userFriend = dataSnapshot.getValue(User.class);
                if(userFriend.getEmail().startsWith(email)&&userFriend.getEmail()!=user.getEmail()) {
                    serchFriends.add(userFriend);
                    idEmailUsers.put(userFriend.getEmail(), dataSnapshot.getKey());
                    FriendSearchActivity.loadResults();
                }
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

    public static void addFriend(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FriendsTracker");
        myRef.child("Friends").child(users.getUid()).child(id).setValue(true);
    }

    public static String fromIdToEmail(final String email){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker").child("Users");
        ChildEventListener valueEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                userFriend = dataSnapshot.getValue(User.class);
                if(userFriend.getEmail().equals(email))
                    id=dataSnapshot.getKey();
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
        return id;
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