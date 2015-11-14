package com.example.olamac.inzynier;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Map2 extends FragmentActivity {

    SrodekTransportu[] transports = new SrodekTransportu[10];
    SrodekTransportu przenies = new SrodekTransportu("edward", 10, 10, 111);
    String numer;

    java.util.Map<Integer, Marker> markers = new HashMap<Integer,Marker >();

    boolean flaga=false;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
//    SrodekTransportu[] tablica = new SrodekTransportu[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        Intent intent = getIntent();
        String numer = (String) intent.getSerializableExtra("przenies2");
        this.numer = numer;

//            new NewRequest().execute();
        runThread();

    }

    private void update() {


        for (int i = 0; i < transports.length; i++) {

            if ((transports[i] != null) && (markers.containsKey(transports[i].getK()))) {
                Marker a = markers.get(transports[i].getK());

                Log.d("markers", a.getPosition().toString());
                Log.d("transport", Double.toString(transports[i].getX())+Double.toString(transports[i].getY()));

                a.setPosition(new LatLng(transports[i].getX(), transports[i].getY()));
                // .position(new LatLng(xs,ys))
//                a.setTitle(transports[i].getNazwa());
//                a.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bus));

            }
        }
    }
    private void runThread(){
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

// This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new NewRequest().execute();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

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


        for (int i = 0; i < transports.length; i++) {

            if(transports[i]!= null) {


               Marker marker=
                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(transports[i].getX(), transports[i].getY()))
                                        // .position(new LatLng(xs,ys))
                                .title(transports[i].getNazwa())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))

                );
                markers.put((transports[i].getK()),marker);
                transports[i]=null;
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(transports[0].getX(), transports[0].getY()), 12));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);



    }


    public class NewRequest extends AsyncTask<String, Integer, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(
                        "http://10.0.2.2:8080/");
                Log.d("test","łączy sie");

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                String response = EntityUtils.toString(httpEntity);


                return new JSONArray(response);

            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);


            try {

                int p=0;
                List<JSONObject> a = new ArrayList<JSONObject>();
                for (int i=0;i<result.length(); i++) {
                    a.add(result.getJSONObject(i));
                    //Log.d("test",result.getJSONObject((i)).toString());
                }

                for (int i=0;i<result.length(); i++) {
                    if (numer.equals(a.get(i).getString("name"))) {

                        SrodekTransportu transport = new SrodekTransportu(a.get(i).getString("name"), Double.parseDouble(a.get(i).getString("x")), Double.parseDouble(a.get(i).getString("y")), Integer.parseInt(a.get(i).getString("k")));
                        przenies = transport;

                        transports[p] = transport;
                        p = p + 1;
                    }
                }
                if (flaga==false){
                    setUpMapIfNeeded();
                    flaga=true;
                }else{
                    update();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }





}
