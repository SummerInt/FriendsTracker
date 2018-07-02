package com.teamm.friendstracker.ui;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.entity.User;
import com.teamm.friendstracker.present.RVFriendsAdapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FriendActivity extends AppCompatActivity implements View.OnClickListener{

    //List<User> friends;
    RVFriendsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        RecyclerView rvFriends = (RecyclerView)findViewById(R.id.rvFriends);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(llm);
        adapter = new RVFriendsAdapter();
        rvFriends.setAdapter(adapter);
        loadFriends();
        loadFriends();
        Button bAddFriend = (Button)findViewById(R.id.bAddFriend);
        bAddFriend.setOnClickListener(this);
    }


    private void loadFriends() {
        ArrayList<User> friends = getFriends();
        adapter.setItems(friends);
    }

    private ArrayList<User> getFriends() {
        ArrayList<User> friends = new ArrayList<User>();
        friends.add(new User("serbeznoe_mbIlo@mail.ru", "Иван", "Иванов", false, false));
        friends.add(new User("love_cat@mail.ru", "Лена", "Петрова", false, false));
        return friends;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAddFriend:
                Intent intent = new Intent(this, FriendSearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
