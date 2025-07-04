package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.TourItem;
import at.technikumwien.tourplannerbackend.repository.TourItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TourService {
    private final TourItemRepository repository;
    private final OpenRouteService openRouteService;
    private static final Logger logger = LoggerFactory.getLogger(TourService.class);

    public TourService(TourItemRepository repository, OpenRouteService openRouteService) {
        this.repository = repository;
        this.openRouteService = openRouteService;
    }

    public List<TourItem> getAllTours() {
        logger.info("Get all tours");
        return repository.findAll();
    }

    public TourItem saveTour(TourItem tour) {
        logger.info("Saving new tour: {}", tour);
        double distance = this.openRouteService.calculateDistance(tour.getStartLocation(), tour.getEndLocation());
        tour.setDistance(distance);

        double duration = this.openRouteService.calculateDuration(tour.getStartLocation(), tour.getEndLocation());
        tour.setEstimatedTime(duration);

        logger.debug("Calculated distance: {}", distance);
        logger.debug("Estimated duration: {}", duration);

        TourItem saved = repository.save(tour);
        logger.info("Tour saved successfully with ID: {}", saved.getId());

        return saved;
    }

    public void deleteTour(String id) {
        logger.info("Deleting tour with ID: {}", id);
        repository.deleteById(id);
    }

    public TourItem getTourById(String id) {
        logger.info("Get tour with ID: {}", id);
        return repository.findById(id).orElse(null);
    }
}
