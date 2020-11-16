package com.naer.pdfreader;

import com.google.api.client.json.Json;
import com.google.gson.JsonObject;

public class KeyDatabase {

    public Double Latitude;
    public Double Longitude;
    public int Key;
    public String Place;
    public int Radius = 25;

    public KeyDatabase(Double latitude, Double longitude,int radius, int key,String place) {
        Latitude = latitude;
        Longitude = longitude;
        Radius = radius;
        Key = key;
        Place = place;
    }
}
