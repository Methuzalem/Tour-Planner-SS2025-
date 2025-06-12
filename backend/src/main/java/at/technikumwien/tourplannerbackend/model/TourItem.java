package at.technikumwien.tourplannerbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "tour_items")
public class TourItem {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    private String name;
    private String description;

    @Column(name = "start_location")
    private String from;

    @Column(name = "end_location")
    private String to;

    @Column(name = "transport_type")
    private String transportType;

    private Double distance;

    @Column(name = "estimated_time")
    private String estimatedTime;

    @Column(name = "route_information")
    private String routeInformation;

    @Column(name = "image_url")
    private String imageUrl;

    // No-arg constructor required by Hibernate
    public TourItem() {}

    // All-args constructor
    public TourItem(
            String id,
            String name,
            String description,
            String from,
            String to,
            String transportType,
            Double distance,
            String estimatedTime,
            String routeInformation,
            String imageUrl
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.routeInformation = routeInformation;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Similarly for all other fields...

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getRouteInformation() {
        return routeInformation;
    }

    public void setRouteInformation(String routeInformation) {
        this.routeInformation = routeInformation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
