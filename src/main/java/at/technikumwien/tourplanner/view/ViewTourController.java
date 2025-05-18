package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.viewmodel.ViewTourViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ViewTourController {
    @FXML private Label tourNameLabel;
    @FXML private Label tourDescriptionLabel;
    @FXML private Label fromLabel;
    @FXML private Label toLabel;
    @FXML private Label transportTypeLabel;
    @FXML private Label distanceLabel;
    @FXML private Label estimatedTimeLabel;
    @FXML private Label routeInformationLabel;
    @FXML private Button editButton;
    
    private final ViewTourViewModel viewModel;

    public ViewTourController(ViewTourViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @FXML
    public void initialize() {
        // Bind all tour details to their corresponding labels using StringBinding
        tourNameLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? tour.name() : "No tour selected";
                },
                viewModel.currentTourProperty()
            )
        );
        
        tourDescriptionLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? 
                        (tour.description() != null && !tour.description().isEmpty() ? 
                            tour.description() : "No description available") : 
                        "No tour selected";
                },
                viewModel.currentTourProperty()
            )
        );
        
        fromLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? 
                        (tour.from() != null && !tour.from().isEmpty() ? 
                            tour.from() : "Not specified") : 
                        "";
                },
                viewModel.currentTourProperty()
            )
        );
        
        toLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? 
                        (tour.to() != null && !tour.to().isEmpty() ? 
                            tour.to() : "Not specified") : 
                        "";
                },
                viewModel.currentTourProperty()
            )
        );
        
        transportTypeLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? 
                        (tour.transportType() != null && !tour.transportType().isEmpty() ? 
                            tour.transportType() : "Not specified") : 
                        "";
                },
                viewModel.currentTourProperty()
            )
        );
        
        distanceLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? 
                        (tour.distance() != null ? 
                            tour.distance().toString() : "Not specified") : 
                        "";
                },
                viewModel.currentTourProperty()
            )
        );
        
        estimatedTimeLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? 
                        (tour.estimatedTime() != null && !tour.estimatedTime().isEmpty() ? 
                            tour.estimatedTime() : "Not specified") : 
                        "";
                },
                viewModel.currentTourProperty()
            )
        );
        
        routeInformationLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? 
                        (tour.routeInformation() != null && !tour.routeInformation().isEmpty() ? 
                            tour.routeInformation() : "No route information available") : 
                        "";
                },
                viewModel.currentTourProperty()
            )
        );
        
        // Disable the edit button if no tour is selected
        editButton.disableProperty().bind(Bindings.isNull(viewModel.currentTourProperty()));
    }
    
    @FXML
    protected void onEditButtonClick() {
        // Call the view model method to switch to edit mode for the current tour
        viewModel.editCurrentTour();
    }
}
