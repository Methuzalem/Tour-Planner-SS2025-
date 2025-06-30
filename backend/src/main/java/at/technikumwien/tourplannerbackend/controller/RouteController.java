package at.technikumwien.tourplannerbackend.controller;

import at.technikumwien.tourplannerbackend.model.Location;
import at.technikumwien.tourplannerbackend.service.OpenRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RouteController {

    private final OpenRouteService openRouteService;

    @Autowired
    public RouteController(OpenRouteService openRouteService) {
        this.openRouteService = openRouteService;
    }

    @PostMapping("/location-autocomplete")
    public List<Location> getAutocomplete(@RequestBody String query) {
        return openRouteService.getLocationSuggestions(query);
    }
}
