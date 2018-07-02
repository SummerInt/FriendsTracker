package com.teamm.friendstracker.ui;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.db.DbManager;
import com.teamm.friendstracker.model.entity.User;
import com.teamm.friendstracker.present.RVFriendsAdapter;

import android.content.Intent;
import android.os.Handler;
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

    RecyclerView rvFriends;

    private int mInterval = 2000;
    private Handler mHandler;
    private Runnable mAdShower = new Runnable() {
        @Override
        public void run() {
            try {


                loadFriends();


            } finally {
                mHandler.postDelayed(mAdShower, mInterval);
            }
        }
    };

    private void startTask() {
        mAdShower.run();
    }

    private void stopTask() {

        mHandler.removeCallbacks(mAdShower);
    }
    //List<User> friends;
    RVFriendsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        DbManager.readFriends();
        rvFriends = (RecyclerView)findViewById(R.id.rvFriends);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(llm);
        adapter = new RVFriendsAdapter();


        Button bAddFriend = (Button)findViewById(R.id.bAddFriend);
        bAddFriend.setOnClickListener(this);
        //rvFriends.clear();
        DbManager.friends.clear();
        mHandler = new Handler();
        startTask();
    }


    private void loadFriends() {
        adapter.clearItems();
        adapter.setItems(DbManager.friends);
        rvFriends.setAdapter(adapter);
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