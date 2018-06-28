package com.teamm.friendstracker.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamm.friendstracker.R;
import com.teamm.friendstracker.view.MapView;

public class MapActivity extends AppCompatActivity implements MapView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //DrawerLayout navigationLayout = findViewById(R.id.navigation_layout);
        ListView navigationListView = findViewById(R.id.navigation_list_view);
        String[] navigationTitles = getResources().getStringArray(R.array.navigation_title_array);

        navigationListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.navigation_list_item, navigationTitles));
        //navigationListView.setOnItemClickListener(new DrawerItemClickListener());
    }
}
