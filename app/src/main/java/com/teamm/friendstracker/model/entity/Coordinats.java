package com.teamm.friendstracker.model.entity;

public class Coordinats {
    private double latitude;
    private double longitude;
    private String id;

    public Coordinats(){

    }

    public Coordinats(double latitude, double longitude, String id){
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

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

    public String getKey() {
        return id;
    }

    public void setKey(String id) {
        this.id = id;
    }
}
