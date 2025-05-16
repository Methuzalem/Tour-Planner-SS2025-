package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.TourItem;
import at.technikumwien.tourplanner.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainController implements PropertyChangeListener {
    private final MainViewModel viewModel;

    public MainController(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    private ListView<TourItem> tourList = new ListView<>();

    @FXML
    public void initialize() {
        // Bind the tour list to the list view
        tourList.setItems(viewModel.getTourList());
        tourList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSelectedTourItem(newValue);
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("newTour".equals(evt.getPropertyName())) {
            // When a new tour is added, select it in the list
            tourList.getSelectionModel().select((TourItem) evt.getNewValue());
        }
    }
}
