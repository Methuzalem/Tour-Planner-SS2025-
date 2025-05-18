package at.technikumwien.tourplanner.model;

import java.util.UUID;

public record TourItem(
        String id,
        String name,
        String description,
        String from,
        String to,
        String transportType,
        Double distance,
        String estimatedTime,
        String routeInformation) {
    
    public TourItem(String name) {
        this(UUID.randomUUID().toString(), name, "", "", "", "", 0.0, "", "");
    }
    
    // Constructor without ID for creating new tours (ID will be assigned by the TourManager)
    public TourItem(
            String name, 
            String description, 
            String from, 
            String to, 
            String transportType, 
            Double distance, 
            String estimatedTime, 
            String routeInformation) {
        this(UUID.randomUUID().toString(), name, description, from, to, transportType, distance, estimatedTime, routeInformation);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
