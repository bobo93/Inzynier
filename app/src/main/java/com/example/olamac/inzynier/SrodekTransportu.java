package com.example.olamac.inzynier;

import java.io.Serializable;

/**
 * Created by olamac on 13.11.2015.
 */

public class SrodekTransportu implements Serializable {
    private String nazwa;
    private double x;
    private double y;
    private int k;
    SrodekTransportu(String nazwa, double x, double y, int k){
        this.nazwa = nazwa;
        this.x = x;
        this.y = y;
        this.k=k;

    }

    public double getY(){
        return y;
    }

    public double getX(){
        return x;
    }

    public String getNazwa(){
        return nazwa;
    }
    public int getK() {
        return k;
    }


}
