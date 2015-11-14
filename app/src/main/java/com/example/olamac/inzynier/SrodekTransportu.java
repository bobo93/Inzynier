package com.example.olamac.inzynier;

import java.io.Serializable;

/**
 * Created by olamac on 13.11.2015.
 */

public class SrodekTransportu implements Serializable {
    private String nazwa;
    private double x;
    private double y;

    SrodekTransportu(String nazwa, double x, double y){
        this.nazwa = nazwa;
        this.x = x;
        this.y = y;

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



}
