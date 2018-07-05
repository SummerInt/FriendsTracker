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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FriendActivity extends AppCompatActivity implements View.OnClickListener, RVFriendsAdapter.OnCardClickListener{

    static RecyclerView rvFriends;

    static RVFriendsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        rvFriends = (RecyclerView)findViewById(R.id.rvFriends);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(llm);
        adapter = new RVFriendsAdapter();
        adapter.setOnCardClickListener(this);

        Button bAddFriend = (Button)findViewById(R.id.bAddFriend);
        bAddFriend.setOnClickListener(this);
    }

    @Override
    public void onCardClick(View view,final int index) {
        showPopupMenu(view, index);
    }

    private void showPopupMenu(View v, int i) {
        final int index = i;
        PopupMenu popupMenu = new PopupMenu(FriendActivity.this, v);
        popupMenu.inflate(R.menu.popup_menu_friends);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        //Удаленпие друга тут
                        Toast.makeText(getApplicationContext(), "Удалено", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        DbManager.readFriendId();
        DbManager.readFriends();
        DbManager.friends.clear();
    }

    public static void loadFriends() {
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