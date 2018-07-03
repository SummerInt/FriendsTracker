package com.teamm.friendstracker.ui;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.teamm.friendstracker.view.MapView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.db.DbManager;

public class MainActivity extends AppCompatActivity
        implements MapView, OnMapReadyCallback, LocationListener, NavigationView.OnNavigationItemSelectedListener{

    static final int RED_PROF_ACTIVITY_REQUEST = 2;

    View header;

    ImageView photo;
    int photoRes;

    GoogleMap map;

    private int mInterval = 2000;
    private Handler mHandler;
    private Runnable mAdShower = new Runnable() {
        @Override
        public void run() {
            try {

                setProfileInfo();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);

        photoRes = R.id.ivPhoto;
        photo = header.findViewById(photoRes);

        setProfileInfo();

        LinearLayout menuHeadLayout = (LinearLayout) header.findViewById(R.id.mHeadLayout);
        menuHeadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.mHeadLayout) {
                    Intent intent = new Intent(MainActivity.this, ProfileEditorActivity.class);
                    setResult(RESULT_OK, intent);
                    startActivityForResult(intent, RED_PROF_ACTIVITY_REQUEST);
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DbManager.read();
        mHandler = new Handler();
        startTask();

    }

    private void setProfileInfo(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        TextView mail=(TextView) header.findViewById(R.id.tvMail);
        TextView name=(TextView) header.findViewById(R.id.tvName);
        String nameAndSurname = DbManager.user.getName()+" "+DbManager.user.getSurname();
        name.setText(nameAndSurname);
        mail.setText(DbManager.user.getEmail());

        /*Glide.with(this)
                .load(DbManager.avatarDownload())
                .into(photo);*/

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(DbManager.getAvatarStorageReference())
                .into(photo);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friends) {
            Intent intent = new Intent(this, FriendActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            DbManager.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RED_PROF_ACTIVITY_REQUEST:
                if (resultCode == RESULT_OK) {
                    ImageView iv = (ImageView) findViewById(R.id.ivPhoto);
                    TextView tvName  = (TextView) findViewById(R.id.tvName);
                    String uriStr = data.getStringExtra("photo");
                    if(!uriStr.isEmpty()){
                        Uri selectedImage = Uri.parse(uriStr);
                        iv.setImageURI(null);
                        iv.setImageURI(selectedImage);
                    }
                    String name = data.getStringExtra("name");
                    String surname = data.getStringExtra("surname");
                    tvName.setText(name+" "+surname);
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        map.setMinZoomPreference(10);

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET};

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, RESULT_FIRST_USER);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                });

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat
                .checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, RESULT_FIRST_USER);

            return;
        }

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

        //DbManager.user.getAvatar()
        //Uri uri = DbManager.avatarDownload();

        map.addMarker(new MarkerOptions()
                .position(position)
                //.snippet("Я")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                //.icon(BitmapDescriptorFactory
                //        .fromBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.degault_prof_photo))
                //)
                //.icon(BitmapDescriptorFactory
                //        .fromBitmap(BitmapFactory.decodeResource(this.getResources(), photoRes))
                //)
        );
        map.moveCamera(CameraUpdateFactory.newLatLng(position));

        map.addCircle(new CircleOptions()
                .center(position)
                .radius(15000)
                .strokeColor(Color.BLACK)
                .fillColor(/*R.color.colorVisibilityRadius*/0x10ff0000)
                .strokeWidth(2));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}