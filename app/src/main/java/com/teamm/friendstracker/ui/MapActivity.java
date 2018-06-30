package com.teamm.friendstracker.ui;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamm.friendstracker.R;
import com.teamm.friendstracker.view.MapView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements MapView, OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //DrawerLayout navigationLayout = findViewById(R.id.navigation_layout);
        ListView navigationListView = findViewById(R.id.navigation_list_view);
        String[] navigationTitles = getResources().getStringArray(R.array.navigation_title_array);

        navigationListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.navigation_list_item, navigationTitles));
        //navigationListView.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
