package com.example.olamac.inzynier;


import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Map1 extends Fragment {

    public static Map1 newInstance() {
        Map1 fragment = new Map1();
        return fragment;
    }

    public Map1() {
    }

    Button ClickMe;
    TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_map1, container, false);


        return rootView;
    }



}
