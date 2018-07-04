package com.teamm.friendstracker.ui;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.db.DbManager;
import com.teamm.friendstracker.model.entity.User;
import com.teamm.friendstracker.present.RVSearchAdapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class FriendSearchActivity extends AppCompatActivity implements View.OnClickListener{

    static RVSearchAdapter adapter;
    EditText etSearch;
    private static RecyclerView rvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        rvSearch = (RecyclerView)findViewById(R.id.rvResults);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvSearch.setLayoutManager(llm);
        adapter = new RVSearchAdapter();
        etSearch = (EditText)findViewById(R.id.etSearch);
        Button bSearch = (Button)findViewById(R.id.bSearch);
        bSearch.setOnClickListener(this);

        DbManager.serchFriends.clear();
    }



    public static void loadResults() {
        adapter.clearItems();
        adapter.setItems(DbManager.serchFriends);
        rvSearch.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSearch:
                DbManager.idEmailUsers.clear();
                DbManager.serchFriends.clear();
                DbManager.serchFriend(etSearch.getText().toString());
                //Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
