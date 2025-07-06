package at.technikumwien.tourplanner.model;

import java.util.UUID;

public record TourItem(
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
    
    public TourItem(String name) {
        this(UUID.randomUUID().toString(), name, "", null, null, "", 0.0, 0, "");
    }
    
    // Constructor without ID for creating new tours (ID will be assigned by the TourManager)
    public TourItem(
            String name,
            String description,
            Location startLocation,
            Location endLocation,
            String transportType,
            Double distance,
            Integer estimatedTime,
            String routeInformation
            ) {
        this(UUID.randomUUID().toString(), name, description, startLocation, endLocation, transportType, distance, estimatedTime, routeInformation);
    }

    //Constructor with ID endLocation be able endLocation hardcode the relationships between logs and tours
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
    
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
