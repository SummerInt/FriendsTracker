package com.teamm.friendstracker.model.db;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.teamm.friendstracker.ui.MainActivity;

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



    public static ArrayList<User> friends = new ArrayList<User>();

    public static ArrayList<String> friendsId = new ArrayList<String>();

    public static ArrayList<Coordinats> coordinats = new ArrayList<Coordinats>();

    public static ArrayList<User> serchFriends = new ArrayList<User>();

    public static HashMap<String,String> idEmailUsers = new HashMap<>();

    public static HashMap<String,String> idEmailFriends = new HashMap<>();

    private final Listener listener;

    public DbManager(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onFriendsCoordLoad(Coordinats coord);

        void onPhotoDownload(byte[] bytes);
    }

    public static void write() {
        if (user.getName() != null && user.getEmail() != null && user.getSurname() != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("FriendsTracker");
            users = mAuth.getCurrentUser();
            myRef.child("Users").child(users.getUid()).setValue(user);
        }
    }

    public static void read(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        users = mAuth.getCurrentUser();
        final DatabaseReference myRef = database.getReference("FriendsTracker");
        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child("Users").child(users.getUid()).getValue(User.class);
                if(user.isOnline() && MainActivity.switch_button != null)
                    MainActivity.switch_button.setChecked(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        DbManager.readFriendId();
    }

    public static void deleteFriend(User us){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        int index = friends.indexOf(us);
        String usId = friendsId.get(index);
        DatabaseReference myRef = database.getReference("FriendsTracker");
        myRef.child("Friends").child(users.getUid()).child(usId).removeValue();

        //friends.remove(index);
        //friendsId.remove(index);
        //idEmailFriends.remove(index);

        friends.clear();
        friendsId.clear();
        idEmailFriends.clear();

        readFriendId();
        readFriends();

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
                String id = dataSnapshot.getKey();
                if(friendsId.indexOf(id)!=-1) {
                    friends.add(userFriend);
                    FriendActivity.friends.add(userFriend);
                    FriendActivity.loadFriends();
                    idEmailFriends.put(id,userFriend.getEmail());
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
        if(user.isOnline()) {
            Coordinats coordinats = new Coordinats(latitude, longitude, users.getUid());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("FriendsTracker");
            myRef.child("Coordinats").child(users.getUid()).setValue(coordinats);
        }
    }

    public void readCoordinats(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker").child("Coordinats");
        ChildEventListener valueEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //coordinats.add(dataSnapshot.getValue(Coordinats.class));
                if(friendsId.indexOf(dataSnapshot.getKey())!= -1) {

                    HashMap map = (HashMap) dataSnapshot.getValue();
                    if (map != null) {
                        Coordinats coordinat = new Coordinats((double) map.get("latitude"), (double) map.get("longitude"), (String) map.get("id"));

                        listener.onFriendsCoordLoad(coordinat);

                    }
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

    public static void serchFriend(final String email){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("FriendsTracker").child("Users");
        ChildEventListener valueEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                userFriend = dataSnapshot.getValue(User.class);
                if (userFriend.getEmail().startsWith(email) && userFriend.getEmail() != user.getEmail() && friendsId.indexOf(key) == -1) {
                    serchFriends.add(userFriend);
                    idEmailUsers.put(userFriend.getEmail(), key);
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
    
    public void downloadPhoto() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(DbManager.users.getUid()).child("avatar.jpg");


        pathReference
                .getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        listener.onPhotoDownload(bytes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }
}
