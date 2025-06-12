package at.technikumwien.tourplannerbackend.model;

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
        String routeInformation,
        String imageUrl
) {
    // Constructor without ID for creating new tours (ID will be assigned by the TourManager)
    public TourItem(
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
        this(UUID.randomUUID().toString(), name, description, from, to, transportType, distance, estimatedTime, routeInformation, imageUrl);
    }

    //Constructor with ID to be able to hardcode the relationships between logs and tours
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

    @Override
    public String toString() {
        return name;
    }
}
