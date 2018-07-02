package com.teamm.friendstracker.ui;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.entity.User;
import com.teamm.friendstracker.present.RVSearchAdapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class FriendSearchActivity extends AppCompatActivity implements View.OnClickListener{

    //List<User> friends;
    RVSearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        RecyclerView rvSearch = (RecyclerView)findViewById(R.id.rvResults);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvSearch.setLayoutManager(llm);
        adapter = new RVSearchAdapter();
        rvSearch.setAdapter(adapter);
        Button bSearch = (Button)findViewById(R.id.bSearch);
        bSearch.setOnClickListener(this);
    }


    private void loadResults() {
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
            case R.id.bSearch:
                Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
                loadResults();
                break;
        }
    }

}