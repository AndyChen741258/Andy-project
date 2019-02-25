package com.naer.pdfreader;

public class Place {
    private int id;
    public static String GlobalPlace;
    public static int Radius = 25;

    public Place() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLag() {
        return Lag;
    }

    public void setLag(Double lag) {
        Lag = lag;
    }

    private Double lat;
    private Double Lag;
}
