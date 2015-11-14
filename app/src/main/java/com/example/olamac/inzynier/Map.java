package com.example.olamac.inzynier;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Map extends Fragment implements View.OnClickListener {


    SrodekTransportu[] transports = new SrodekTransportu[10];

    EditText edit;
    Button map;
    Button wybierz;
    public static Map newInstance() {
        Map fragment = new Map();
        return fragment;
    }

    public Map() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_map1, container, false);
        EditText edit = (EditText)rootView.findViewById(R.id.editText);
        this.edit = edit;
        Button map = (Button) rootView.findViewById(R.id.button4);
        this.map=map;
        Button wybierz = (Button) rootView.findViewById(R.id.button);
        this.wybierz=wybierz;
        map.setOnClickListener(this);
        wybierz.setOnClickListener(this);
//        map.setEnabled(false);


        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button4:

//
//                Log.d("tran", String.valueOf(transports[0].getX()));
//                for(int a = 0; a<transports.length; a++) {
//                    Log.d("map", String.valueOf(transports[a] == null ? a : transports[a].getX()));
//                }
//                Intent k = new Intent(getActivity(), Map2.class);
//                k.putExtra("przenies", transports);
//                k.putExtra("przenies2", "lallaa");
                Intent k = new Intent(getActivity(), Map2.class);
                k.putExtra("przenies2", "249");
                getActivity().startActivity(k);
                break;
            case R.id.button:


                map.setEnabled(true);



        }

    }
}
