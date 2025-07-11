package at.technikumwien.tourplannerbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.util.ArrayList;
import java.util.List;

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
    private Double estimatedTime;

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
            Double estimatedTime,
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

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getRouteInformation() {
        return routeInformation;
    }

    public void setRouteInformation(String routeInformation) {
        this.routeInformation = routeInformation;
    }

    /**
     * Returns a CSV formatted string for export purposes.
     * Format: T,id,name,description,startLocationName,startLocationLat,startLocationLng,endLocationName,endLocationLat,endLocationLng,transportType,distance,estimatedTime,routeInformation
     */
    @JsonIgnore
    public String getExportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("T,");
        sb.append(escapeField(id)).append(",");
        sb.append(escapeField(name)).append(",");
        sb.append(escapeField(description)).append(",");

        // Start location
        sb.append(escapeField(startLocation != null ? startLocation.getDisplayName() : "")).append(",");
        sb.append(startLocation != null ? startLocation.getLatitude() : "").append(",");
        sb.append(startLocation != null ? startLocation.getLongitude() : "").append(",");

        // End location
        sb.append(escapeField(endLocation != null ? endLocation.getDisplayName() : "")).append(",");
        sb.append(endLocation != null ? endLocation.getLatitude() : "").append(",");
        sb.append(endLocation != null ? endLocation.getLongitude() : "").append(",");

        sb.append(escapeField(transportType)).append(",");
        sb.append(distance != null ? distance : "").append(",");
        sb.append(estimatedTime != null ? estimatedTime : "").append(",");
        sb.append(escapeField(routeInformation));

        return sb.toString();
    }

    // Helper method to escape commas in fields
    private String escapeField(String field) {
        if (field == null) {
            return "";
        }
        // If the field contains commas or quotes, wrap it in quotes and escape any quotes
        if (field.contains(",") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    /**
     * Creates a TourItem from an export string
     * @param exportString The export string in CSV format
     * @return A new TourItem instance
     */
    public static TourItem fromExportString(String exportString) {
        // Parse CSV line, handling quoted fields
        List<String> fields = parseCsvLine(exportString);

        if (fields.size() < 14 || !fields.get(0).equals("T")) {
            throw new IllegalArgumentException("Invalid tour export string format");
        }

        int index = 1; // Skip the type field

        String id = fields.get(index++);
        String name = fields.get(index++);
        String description = fields.get(index++);

        // Start location
        String startLocName = fields.get(index++);
        double startLat = fields.get(index).isEmpty() ? 0 : Double.parseDouble(fields.get(index++));
        double startLng = fields.get(index).isEmpty() ? 0 : Double.parseDouble(fields.get(index++));
        Location startLocation = new Location(startLocName, startLat, startLng);

        // End location
        String endLocName = fields.get(index++);
        double endLat = fields.get(index).isEmpty() ? 0 : Double.parseDouble(fields.get(index++));
        double endLng = fields.get(index).isEmpty() ? 0 : Double.parseDouble(fields.get(index++));
        Location endLocation = new Location(endLocName, endLat, endLng);

        String transportType = fields.get(index++);
        Double distance = fields.get(index).isEmpty() ? null : Double.parseDouble(fields.get(index++));
        Double estimatedTime = fields.get(index).isEmpty() ? null : Double.parseDouble(fields.get(index++));
        String routeInformation = fields.get(index);

        return new TourItem(
            id, name, description, startLocation, endLocation,
            transportType, distance, estimatedTime, routeInformation
        );
    }

    /**
     * Parses a CSV line into fields, handling quoted values
     */
    private static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Check if this is an escaped quote
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++; // Skip the next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }

        // Add the last field
        result.add(field.toString());
        return result;
    }
}
