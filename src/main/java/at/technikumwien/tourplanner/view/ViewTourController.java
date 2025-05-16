package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.viewmodel.ViewTourViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewTourController {
    @FXML
    private Label tourNameLabel;
    
    @FXML
    private Label tourDetailsLabel;
    
    private final ViewTourViewModel viewModel;

    public ViewTourController(ViewTourViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @FXML
    public void initialize() {
        // Bind the tour name label to the name property of the current tour
        tourNameLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? tour.name() : "No tour selected";
                },
                viewModel.currentTourProperty()
            )
        );
        
        // Set up a listener for currentTour changes to update the details label
        /*viewModel.currentTourProperty().addListener((observable, oldTour, newTour) -> {
            if (newTour != null) {
                tourDetailsLabel.setText(newTour.getDescription() != null ? 
                    newTour.getDescription() : "No description available");
            } else {
                tourDetailsLabel.setText("No tour selected");
            }
        });*/
    }
}
