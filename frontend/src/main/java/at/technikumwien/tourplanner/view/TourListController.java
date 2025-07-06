package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.viewmodel.TourListViewModel;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;

public class TourListController {
    @FXML
    private ListView<TourItem> tourList;

    @FXML
    private TextField searchField;

    private final TourListViewModel viewModel;
    private FilteredList<TourItem> filteredTours;

    public TourListController(TourListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        filteredTours = new FilteredList<>(viewModel.getTourList(), p -> true);
        tourList.setItems(filteredTours);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTours.setPredicate(tour -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return tour.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        tourList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                viewModel.selectTour(newValue);
            }
        });
    }

    @FXML
    private void onAddTourButtonClick() {
        viewModel.createNewTour();
    }

    @FXML
    private void onExportButtonClick() {
        viewModel.exportTours();
    }

    @FXML
    private void onImportButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Tours");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TourPlanner Export Files (*.tpexp)", "*.tpexp"));

        File userDir = new File(System.getProperty("user.home"), "Downloads/Tourplanner");
        if (!userDir.exists()) {
            userDir = new File(System.getProperty("user.home"), "Downloads");
        }
        if (userDir.exists()) {
            fileChooser.setInitialDirectory(userDir);
        }

        File selectedFile = fileChooser.showOpenDialog(tourList.getScene().getWindow());
        if (selectedFile != null) {
            viewModel.importTours(selectedFile);
        }
    }

    @FXML
    protected void onSummaryReportButtonClick() {
        viewModel.generateSummaryReport();
    }
}
