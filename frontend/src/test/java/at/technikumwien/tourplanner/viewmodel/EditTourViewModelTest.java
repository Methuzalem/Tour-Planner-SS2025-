package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.Location;
import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.RouteService;
import at.technikumwien.tourplanner.service.TourManager;
import at.technikumwien.tourplanner.utils.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EditTourViewModelTest {

    private EditTourViewModel viewModel;
    private TourManager tourManagerMock;
    private RouteService routeServiceMock;

    @BeforeEach
    public void setup() {
        tourManagerMock = mock(TourManager.class);
        routeServiceMock = mock(RouteService.class);
        viewModel = new EditTourViewModel(tourManagerMock, routeServiceMock);
    }

    @Test
    public void testLoadTour_SetsPropertiesCorrectly() {
        Location from = new Location("Start", 10.0, 20.0);
        Location to = new Location("End", 30.0, 40.0);
        TourItem item = new TourItem("1", "Tourname", "Beschreibung", from, to, "Car", 0.0, 0, "Route");

        viewModel.loadTour(item);

        assertEquals("Tourname", viewModel.nameProperty().get());
        assertEquals("Beschreibung", viewModel.descriptionProperty().get());
        assertEquals(from, viewModel.fromProperty().get());
        assertEquals(to, viewModel.toProperty().get());
        assertEquals("Car", viewModel.transportTypeProperty().get());
        assertEquals("Route", viewModel.routeInformationProperty().get());
    }

    @Test
    public void testSaveTour_CallsTourManagerAndResetsFields() {
        Location from = new Location("Start", 10.0, 20.0);
        Location to = new Location("End", 30.0, 40.0);

        viewModel.nameProperty().set("MeineTour");
        viewModel.descriptionProperty().set("Testbeschreibung");
        viewModel.fromProperty().set(from);
        viewModel.toProperty().set(to);
        viewModel.transportTypeProperty().set("Walk");
        viewModel.routeInformationProperty().set("Info");

        viewModel.saveTour();

        verify(tourManagerMock, times(1)).saveTour(any(TourItem.class));

        assertEquals("", viewModel.nameProperty().get());
        assertNull(viewModel.fromProperty().get());
        assertEquals("", viewModel.transportTypeProperty().get());
    }

    @Test
    public void testCancelEdit_ResetsFieldsAndNotifiesListeners() {
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        viewModel.addCancelEditListener(listener);

        viewModel.nameProperty().set("Test");
        viewModel.cancelEdit();

        verify(listener, times(1)).propertyChange(any(PropertyChangeEvent.class));
        assertEquals("", viewModel.nameProperty().get());
    }

    @Test
    public void testFetchFromSuggestions_SchedulesTask() {
        viewModel.fetchFromLocationSuggestions("Vienna");
        // just test if there is an exception
        assertTrue(true);
    }
}
