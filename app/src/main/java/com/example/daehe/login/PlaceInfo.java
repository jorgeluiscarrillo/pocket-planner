package com.example.daehe.login;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {

    private String id;
    private String name;
    private String address;
    private LatLng latLng;
    private String attributions;

    public PlaceInfo(String id, String name, String address, LatLng latLng, String attributions) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
        this.attributions = attributions;
    }

    public PlaceInfo() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", latLng=" + latLng +
                ", attributions='" + attributions + '\'' +
                '}';
    }
}
