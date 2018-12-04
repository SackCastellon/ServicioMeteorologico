package es.uji.ei1048.meteorologia.model;

public final class Coordinates {
    private final float lon;
    private final float lat;

    public Coordinates(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public float getLongitude() {
        return lon;
    }

    public float getLatitude() {
        return lat;
    }
}
