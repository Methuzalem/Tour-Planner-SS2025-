package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.Location;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenRouteService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private static final Logger logger = LoggerFactory.getLogger(OpenRouteService.class);

    public OpenRouteService(@Value("${openrouteservice.api.key}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.apiKey = apiKey;
    }

    // Method to get location suggestions with coordinates
    public List<Location> getLocationSuggestions(String query) {
        // create an http client to make requests to OpenRouteService API
        List<Location> locations = new ArrayList<>();

        try {
            // make an API call to OpenRouteService to get 5 location suggestions
        String autocompleteUrl = "https://api.openrouteservice.org/geocode/autocomplete";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(autocompleteUrl)
                    .queryParam("api_key", apiKey)
                    .queryParam("text", query)
                    .queryParam("size", 5);

            //logging
            String fullUrl = builder.toUriString();
            logger.debug("Requesting location suggestions from OpenRouteService: {}", fullUrl);

            // Send request
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    String.class);

            // parse the response using jackson
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode features = rootNode.path("features");

            // Extract location data
            for (JsonNode feature : features) {
                // Extract coordinates (longitude, latitude order in GeoJSON)
                JsonNode geometryNode = feature.path("geometry");
                JsonNode coordinatesNode = geometryNode.path("coordinates");

                double longitude = 0;
                double latitude = 0;
                if (!coordinatesNode.isMissingNode() && coordinatesNode.isArray() && coordinatesNode.size() >= 2) {
                    longitude = coordinatesNode.get(0).asDouble();
                    latitude = coordinatesNode.get(1).asDouble();
                }

                // Extract location name components
                JsonNode properties = feature.path("properties");
                String name = properties.path("name").asText("");
                String county = properties.path("county").asText("");
                String region = properties.path("region").asText("");
                String country = properties.path("country").asText("");

                // Build the display name
                StringBuilder displayNameBuilder = new StringBuilder(name);

                if (!county.isEmpty() && !county.equals(name)) {
                    displayNameBuilder.append(", ").append(county);
                }

                if (!region.isEmpty() && !region.equals(county) && !region.equals(name)) {
                    displayNameBuilder.append(", ").append(region);
                }

                if (!country.isEmpty()) {
                    displayNameBuilder.append(", ").append(country);
                }

                // Create and add the Location object
                locations.add(new Location(displayNameBuilder.toString(), latitude, longitude));
            }
            logger.info("Found {} location suggestions for query: '{}'", locations.size(), query);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to fetch location suggestions for query '{}': {}", query, e.getMessage());
        }
        // return the list of location objects
        return locations;
    }

    /**
     * Calculates the distance between two locations in meters.
     *
     * @param from The starting location
     * @param to The destination location
     * @return Distance in meters
     */
    public double calculateDistance(Location from, Location to) {
        JsonNode summary = getRouteData(from, to);

        //logging
        double duration = summary != null ? summary.path("duration").asDouble() : -1;
        logger.debug("Estimated duration from {} to {}: {} seconds", from.getDisplayName(), to.getDisplayName(), duration);

        return summary.path("distance").asDouble();
    }

    /**
     * Calculates the travel duration between two locations in seconds.
     *
     * @param from The starting location
     * @param to The destination location
     * @return Duration in seconds
     */
    public double calculateDuration(Location from, Location to) {
        JsonNode summary = getRouteData(from, to);
            return summary.path("duration").asDouble();
    }

    /**
     * Helper method to retrieve route data from the OpenRouteService API.
     */
    private JsonNode getRouteData(Location from, Location to/*, String transportType*/) {
        try {
            String directionsUrl = "https://api.openrouteservice.org/v2/directions/driving-car";

            // Create the request body with coordinates in [longitude, latitude] format
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(directionsUrl)
                    .queryParam("api_key", apiKey)
                    .queryParam("start", from.getLongitude() + "," + from.getLatitude())
                    .queryParam("end", to.getLongitude() + "," + to.getLatitude());

            //logging
            String fullUrl = builder.toUriString();
            logger.debug("Requesting route from OpenRouteService: {}", fullUrl);

            // Make the request
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    String.class);

            // Parse the response
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            // get the summary via features[0].properties.summary
            return rootNode.path("features").get(0).path("properties").path("summary");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to retrieve route data from {} to {}: {}", from, to, e.getMessage());
            return null;
        }
    }
}
