package com.teamm.friendstracker.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.entity.User;
import com.teamm.friendstracker.present.RVAdapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    //List<User> friends;
    RVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        RecyclerView rvFriends = (RecyclerView)findViewById(R.id.rvFriends);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(llm);
        adapter = new RVAdapter();
        rvFriends.setAdapter(adapter);
        loadFriends();
    }


    private void loadFriends() {
        Collection<User> friends = getFriends();
        adapter.setItems(friends);
    }

    private Collection<User> getFriends() {
        return Arrays.asList(
                new User("lol@mail.ru", "Иван", "Иванов",true,false),
                new User("sobaka@mail.ru", "Лена", "Колено",false,false),
                new User("parovoooz@mail.ru", "Антон", "Батон",false,true),
                new User("ded_moroz@mail.ru", "Глеб", "Хлеб",true,true),
                new User("kotik@mail.ru", "Вова", "Головадубова",false,true),
                new User("zhirnotik@mail.ru", "Лешка", "Макарошка",true,false)
        );
    }

}
