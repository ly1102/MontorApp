package com.cuit.monitorapp.main.cuit.monitorapp;

import java.io.Serializable;

public class MarkerInfoUtil implements Serializable {
    private static final long serialVersionUID = 8633299996744734593L;

    private double latitude;
    private double longitude;
    public MarkerInfoUtil(double latitude, double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @Override
    public String toString() {
        return "MarkerInfoUtil [latitude=" + latitude + ", longitude=" + longitude + "]";
    }
    //getter setter
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
