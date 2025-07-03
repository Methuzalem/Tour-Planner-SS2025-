package at.technikumwien.tourplannerbackend.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    private String displayName;
    private Double latitude;
    private Double longitude;

    // No-arg constructor required for JPA
    public Location() {}

    public Location(String displayName, Double latitude, Double longitude) {
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
