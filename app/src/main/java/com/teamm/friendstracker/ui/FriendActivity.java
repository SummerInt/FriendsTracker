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
        Collection<User> friends = getFriends();
        adapter.setItems(friends);
    }

    private Collection<User> getFriends() {
        return Arrays.asList(
                new User("serbeznoe_mbIlo@mail.ru", "Иван", "Иванов", false, false),
                new User("love_cat@mail.ru", "Лена", "Петрова", false, false),
                new User("parovoooz@mail.ru", "Антон", "Батонов",false, false),
                new User("ded_moroz@mail.ru", "Глеб", "Хлебушкин", false, false),
                new User("kotik@mail.ru", "Егор", "Николаев", false, false),
                new User("zhirnotik@mail.ru", "Алексей", "Макарошкин", false, false),
                new User("nashe_vse@mail.ru", "Александр", "Пушкин", false, false)
        );
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
