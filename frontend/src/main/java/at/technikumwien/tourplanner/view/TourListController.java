package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.viewmodel.TourListViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

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
}
