package com.example.olamac.inzynier;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Map extends Fragment implements View.OnClickListener {

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

        Button b = (Button) rootView.findViewById(R.id.btnGoToMap);
        b.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGoToMap:
                Toast.makeText(getActivity(),
                        "Your Message", Toast.LENGTH_LONG).show();

                Intent k = new Intent(getActivity(), Map2.class);
                getActivity().startActivity(k);
                break;
        }

    }
}
