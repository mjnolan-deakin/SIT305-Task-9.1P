package com.mjnolan.restaurantmapapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Boolean showAll;
    SQLiteDatabase sqlDB;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        i = getIntent();

        showAll = i.getBooleanExtra("showAll", true);
        sqlDB = openOrCreateDatabase("LocationsDB",MODE_PRIVATE,null);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (showAll == true) {
            // Show all stored locations
            Cursor cursor = sqlDB.rawQuery("SELECT * FROM locations",null);

            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                Double lat = cursor.getDouble(2);
                Double lon = cursor.getDouble(3);
                Log.i("TESTING", id);
                Log.i("TESTING", name);
                Log.i("TESTING", lat.toString());
                Log.i("TESTING", lon.toString());

                // Add to map
                LatLng newPoint = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions().position(newPoint).title(name));
            }
            cursor.close();
        } else {
            Bundle bundle = i.getExtras();
            String name = bundle.getString("name");
            Double lat = i.getDoubleExtra("lat", -37.8136);
            Double lon = i.getDoubleExtra("lon", 144.9631);

            if (name == null) {
                name = "Melbourne";
            }

            // Add to map
            LatLng newPoint = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(newPoint).title(name));
        }

        // Add a marker in Sydney and move the camera
        LatLng melbourne = new LatLng(-37.8136, 144.9631);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));
    }
}
