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

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenRouteService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private static final String BASE_URL = "https://api.openrouteservice.org/geocode/autocomplete";

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
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("api_key", apiKey)
                    .queryParam("text", query)
                    .queryParam("size", 5);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the list of location objects
        return locations;
    }
}
