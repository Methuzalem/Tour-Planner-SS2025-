package at.technikumwien.tourplannerbackend.service;

import at.technikumwien.tourplannerbackend.model.Location;
import at.technikumwien.tourplannerbackend.model.TourItem;
import at.technikumwien.tourplannerbackend.repository.TourItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TourServiceTest {

    private TourItemRepository repositoryMock;
    private OpenRouteService routeServiceMock;
    private TourService tourService;

    @BeforeEach
    public void setUp() {
        repositoryMock = mock(TourItemRepository.class);
        routeServiceMock = mock(OpenRouteService.class);
        tourService = new TourService(repositoryMock, routeServiceMock);
    }

    @Test
    public void testGetAllTours_ReturnsList() {
        List<TourItem> tours = List.of(new TourItem(), new TourItem());
        when(repositoryMock.findAll()).thenReturn(tours);

        List<TourItem> result = tourService.getAllTours();

        assertEquals(2, result.size());
        verify(repositoryMock).findAll();
    }

    @Test
    public void testSaveTour_ComputesDistanceAndTimeAndSaves() {
        Location start = new Location("Vienna", 48.2, 16.3);
        Location end = new Location("Graz", 47.0, 15.4);

        TourItem tour = new TourItem();
        tour.setStartLocation(start);
        tour.setEndLocation(end);

        when(routeServiceMock.calculateDistance(start, end)).thenReturn(125000.0); // in meters
        when(routeServiceMock.calculateDuration(start, end)).thenReturn(7200.0);   // in seconds

        TourItem saved = new TourItem();
        saved.setId("abc123");
        when(repositoryMock.save(any(TourItem.class))).thenReturn(saved);

        TourItem result = tourService.saveTour(tour);

        assertEquals("abc123", result.getId());
        verify(routeServiceMock).calculateDistance(start, end);
        verify(routeServiceMock).calculateDuration(start, end);
        verify(repositoryMock).save(tour);
        assertEquals(125000.0, tour.getDistance());
        assertEquals(7200.0, tour.getEstimatedTime());
    }

    @Test
    public void testGetTourById_WhenFound() {
        TourItem tour = new TourItem();
        tour.setId("id123");
        when(repositoryMock.findById("id123")).thenReturn(Optional.of(tour));

        TourItem result = tourService.getTourById("id123");

        assertNotNull(result);
        assertEquals("id123", result.getId());
    }

    @Test
    public void testGetTourById_WhenNotFound() {
        when(repositoryMock.findById("unknown")).thenReturn(Optional.empty());

        TourItem result = tourService.getTourById("unknown");

        assertNull(result);
    }

    @Test
    public void testDeleteTour_CallsRepository() {
        tourService.deleteTour("toDelete");

        verify(repositoryMock).deleteById("toDelete");
    }
}
