package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.TourItem;
import at.technikumwien.tourplannerbackend.repository.TourItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TourService {
    private final TourItemRepository repository;
    private final OpenRouteService openRouteService;

    public TourService(TourItemRepository repository, OpenRouteService openRouteService) {
        this.repository = repository;
        this.openRouteService = openRouteService;
    }

    public List<TourItem> getAllTours() {
        return repository.findAll();
    }

    public TourItem saveTour(TourItem tour) {
        double distance = this.openRouteService.calculateDistance(tour.getStartLocation(), tour.getEndLocation());
        tour.setDistance(distance);

        double duration = this.openRouteService.calculateDuration(tour.getStartLocation(), tour.getEndLocation());
        tour.setEstimatedTime(duration);

        System.out.println("tour estimated time is " + tour.getEstimatedTime());
        System.out.println("tour duration is " + tour.getDistance());

        return repository.save(tour);
    }

    public void deleteTour(String id) {
        repository.deleteById(id);
    }

    public TourItem getTourById(String id) {
        return repository.findById(id).orElse(null);
    }
}
