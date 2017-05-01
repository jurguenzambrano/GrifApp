package pe.edu.upc.grifapp.models;

/**
 * Created by Jurguen Zambrano on 1/05/2017.
 */

public class Location {
    double altitude;
    double latitude;

    public double getAltitude() {
        return altitude;
    }

    public Location setAltitude(double altitude) {
        this.altitude = altitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Location setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }
}
