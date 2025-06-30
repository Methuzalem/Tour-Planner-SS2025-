package at.technikumwien.tourplanner.model;

public class Location {
    private double latitude;
    private double longitude;
    private String displayName;

    public Location() {
    }

    public Location(String displayName, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.displayName = displayName;
    }

    public Location(String displayName) {
        this.displayName = displayName;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
