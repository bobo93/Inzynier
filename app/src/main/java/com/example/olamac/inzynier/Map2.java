package com.example.olamac.inzynier;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map2 extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    SrodekTransportu[] tablica = new SrodekTransportu[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);




        Intent intent = getIntent();
        SrodekTransportu[] transports = (SrodekTransportu[]) intent.getSerializableExtra("przenies");
        tablica = transports;
        Log.d("map2", String.valueOf(transports[0].getNazwa()));
       Log.d("map", (String) intent.getSerializableExtra("przenies2"));

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
//       mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));


        for (int i = 0; i < tablica.length; i++) {

            if(tablica[i]!= null) {
                Log.d("setupmap","weszło");
                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(tablica[i].getX(), tablica[i].getY()))
                                        // .position(new LatLng(xs,ys))
                                .title(tablica[i].getNazwa())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))

                );
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(tablica[1].getX(), tablica[1].getY()), 12));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);

    }



}
