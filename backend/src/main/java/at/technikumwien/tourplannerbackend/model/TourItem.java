package at.technikumwien.tourplannerbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Embedded;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.AttributeOverride;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tour_items")
public class TourItem {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;
    private String description;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "displayName", column = @Column(name = "start_location_name")),
        @AttributeOverride(name = "latitude", column = @Column(name = "start_location_lat")),
        @AttributeOverride(name = "longitude", column = @Column(name = "start_location_lng"))
    })
    private Location startLocation;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "displayName", column = @Column(name = "end_location_name")),
        @AttributeOverride(name = "latitude", column = @Column(name = "end_location_lat")),
        @AttributeOverride(name = "longitude", column = @Column(name = "end_location_lng"))
    })
    private Location endLocation;

    @Column(name = "transport_type")
    private String transportType;

    private Double distance;

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Column(name = "route_information")
    private String routeInformation;

    // No-arg constructor required by Hibernate
    public TourItem() {}

    // All-args constructor
    public TourItem(
            String id,
            String name,
            String description,
            Location startLocation,
            Location endLocation,
            String transportType,
            Double distance,
            Integer estimatedTime,
            String routeInformation
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.transportType = transportType;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.routeInformation = routeInformation;
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

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
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

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getRouteInformation() {
        return routeInformation;
    }

    public void setRouteInformation(String routeInformation) {
        this.routeInformation = routeInformation;
    }
}
