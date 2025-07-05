package at.technikumwien.tourplanner.service;

import at.technikumwien.tourplanner.model.Location;
import at.technikumwien.tourplanner.model.TourItem;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TourManagerTest {

    private TourManager tourManager;

    @BeforeEach
    public void setup() {
        // Verhindert HTTP-Aufruf im Konstruktor
        tourManager = new TourManager() {
            {
                getTourList().clear(); // leere Liste für Tests
            }
        };
    }

    @Test
    public void testGetTourList_InitiallyEmpty() {
        assertNotNull(tourManager.getTourList());
        assertEquals(0, tourManager.getTourList().size());
    }

    @Test
    public void testSaveTour_NewTour_AddsToList() {
        Location from = new Location("Vienna", 48.2082, 16.3738);
        Location to = new Location("Graz", 47.0707, 15.4395);
        TourItem newTour = new TourItem(null, "TestTour", "Test", from, to, "Car", 0.0, 0, "RouteInfo");

        TourItem result = tourManager.saveTour(newTour);

        assertNotNull(result);
        assertEquals(1, tourManager.getTourList().size());
    }

    @Test
    public void testDeleteTour_RemovesFromList() {
        TourItem tour = new TourItem("456", "ToDelete", "Info", new Location("A", 1, 1), new Location("B", 2, 2), "Car", 0.0, 0, "Info");
        tourManager.getTourList().add(tour);

        boolean result = tourManager.deleteTour("456");

        assertTrue(result);
        assertTrue(tourManager.getTourList().isEmpty());
    }

    @Test
    public void testDeleteTour_NonExisting_ReturnsFalse() {
        boolean result = tourManager.deleteTour("999");
        assertFalse(result);
    }
}
