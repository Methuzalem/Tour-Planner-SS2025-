package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.Location;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class RouteService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl = "http://localhost:8080";

    public RouteService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get location suggestions based on a search query
     *
     * @param query The search query text
     * @return List of location suggestions
     */
    public List<Location> getLocationSuggestions(String query) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/location-autocomplete"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(query))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

System.out.println("Response status code: " + response.statusCode());
System.out.println("Response body: " + response.body());
                return objectMapper.readValue(response.body(), new TypeReference<List<Location>>() {});
        } catch (Exception e) {
            System.err.println("Error fetching location suggestions: " + e.getMessage());
            e.printStackTrace();
        }

        // Return an empty list if the request fails
        System.out.println("Request failed, returning empty list");
        return new ArrayList<>();
    }
}
