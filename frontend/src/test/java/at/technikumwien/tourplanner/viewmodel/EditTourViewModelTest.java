package at.technikumwien.tourplanner.viewmodel;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.service.RouteService;
import at.technikumwien.tourplanner.service.TourManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class EditTourViewModelTest {

    private EditTourViewModel viewModel;
    private TourManager tourManager;
    private RouteService routeService;


    @BeforeEach
    void setUp() {
        tourManager = mock(TourManager.class);
        routeService = mock(RouteService.class);
        viewModel = new EditTourViewModel(tourManager, routeService);
    }

    @Test
    void testCreateNewTour_savesTour_whenValidInputs() {
        viewModel.nameProperty().set("Test Tour");
        viewModel.fromProperty().set("Vienna");
        viewModel.toProperty().set("Graz");
        viewModel.descriptionProperty().set("Test description");
        viewModel.transportTypeProperty().set("Car");
        viewModel.distanceProperty().set(200.5);
        viewModel.estimatedTimeProperty().set(120);
        viewModel.routeInformationProperty().set("Fastest route");

        viewModel.saveTour();

        verify(tourManager, times(1)).saveTour(any(TourItem.class));
    }

    @Test
    void testCreateNewTour_doesNotSave_whenInvalidInputs() {
        viewModel.nameProperty().set("");  // Invalid (empty)
        viewModel.fromProperty().set("Vienna");
        viewModel.toProperty().set("Graz");
        viewModel.distanceProperty().set(100.0);

        viewModel.saveTour();

        verify(tourManager, never()).saveTour(any());
    }
}
