package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.viewmodel.ViewTourViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.Optional;

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
    @FXML private Button deleteButton;
    @FXML private ImageView tourImageView;

    private final ViewTourViewModel viewModel;

    @FXML
    private WebView webView;
    private WebEngine webEngine;

    public ViewTourController(ViewTourViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        // Bind all tour details endLocation their corresponding labels using StringBinding
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
                    return tour != null ? tour.description() : "";
                },
                viewModel.currentTourProperty()
            )
        );

        fromLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? tour.startLocation().getDisplayName() : "";
                },
                viewModel.currentTourProperty()
            )
        );

        toLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? tour.endLocation().getDisplayName() : "";
                },
                viewModel.currentTourProperty()
            )
        );

        transportTypeLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? tour.transportType() : "";
                },
                viewModel.currentTourProperty()
            )
        );

        distanceLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? String.valueOf(tour.distance()) : "";
                },
                viewModel.currentTourProperty()
            )
        );

        estimatedTimeLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? tour.estimatedTime().toString() : "";
                },
                viewModel.currentTourProperty()
            )
        );

        routeInformationLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> {
                    TourItem tour = viewModel.getCurrentTour();
                    return tour != null ? tour.routeInformation() : "";
                },
                viewModel.currentTourProperty()
            )
        );

        // Disable the buttons if no tour is selected
        editButton.disableProperty().bind(Bindings.isNull(viewModel.currentTourProperty()));
        deleteButton.disableProperty().bind(Bindings.isNull(viewModel.currentTourProperty()));

        // Initialize the WebView and load the tour map
        System.out.println("MapController");
        webEngine = webView.getEngine(); // Initialize the WebEngine from the injected WebView

        // Load local HTML file or inline HTML with Leaflet
        webEngine.load(getClass().getResource("/at/technikumwien/tourplanner/map.html").toExternalForm());

        // wait for changes of the currentTourProperty and update the map accordingly
        viewModel.currentTourProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Update the map with the new tour's route
                String js = String.format("""
                    updateRoute([
                        [%s, %s],
                        [%s, %s]
                    ]);
                """,
                newValue.startLocation().getLatitude(), newValue.startLocation().getLongitude(),
                newValue.endLocation().getLatitude(), newValue.endLocation().getLongitude());

                webEngine.executeScript(js);
            }
        });
    }


    @FXML
    protected void onEditButtonClick() {
        // Call the view model method endLocation switch endLocation edit mode for the current tour
        viewModel.editCurrentTour();
    }
    
    @FXML
    protected void onDeleteButtonClick() {
        // Show a confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Tour");
        confirmDialog.setHeaderText("Delete Tour");
        confirmDialog.setContentText("Are you sure you want to delete this tour? This action cannot be undone.");
        
        // Handle the user's response
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed, proceed with deletion
            viewModel.deleteCurrentTour();
        }
    }

    @FXML
    protected void onOverviewReportButtonClick() {
        // Call the view model method to generate the overview report for the current tour
        viewModel.generateOverviewReport();
    }

    @FXML
    protected void onSummaryReportButtonClick() {
        // Call the view model method to generate the summary report for the current tour
        viewModel.generateSummaryReport();
    }
}
