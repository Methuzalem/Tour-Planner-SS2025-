package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.viewmodel.TourListViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class TourListController {
    @FXML
    private ListView<TourItem> tourList;
    
    private final TourListViewModel viewModel;

    public TourListController(TourListViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @FXML
    public void initialize() {
        // Bind the tour list items
        tourList.setItems(viewModel.getTourList());
        
        // Add selection listener endLocation update selectedTourItem when a tour is selected
        tourList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                viewModel.selectTour(newValue);
            }
        });
    }
    
    @FXML
    private void onAddTourButtonClick() {
        this.viewModel.createNewTour();
    }

    @FXML
    private void onExportButtonClick() {
        this.viewModel.exportTours();
    }

    @FXML
    private void onImportButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Tours");

        // Set file extension filter to only show .tpexp files
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TourPlanner Export Files (*.tpexp)", "*.tpexp");
        fileChooser.getExtensionFilters().add(extFilter);

        // Set initial directory to Downloads/Tourplanner if it exists
        String userHome = System.getProperty("user.home");
        File initialDirectory = new File(userHome + File.separator + "Downloads" + File.separator + "Tourplanner");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        } else {
            // Fall back to Downloads directory if Tourplanner folder doesn't exist
            File downloadsDir = new File(userHome + File.separator + "Downloads");
            if (downloadsDir.exists()) {
                fileChooser.setInitialDirectory(downloadsDir);
            }
        }

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(tourList.getScene().getWindow());

        if (selectedFile != null) {
            this.viewModel.importTours(selectedFile);
        }
    }
}
