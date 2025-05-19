package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.viewmodel.LogListViewModel;
import at.technikumwien.tourplanner.viewmodel.TourListViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class LogListController {
    @FXML
    private ListView<LogItem> logList;

    private final LogListViewModel viewModel;

    public LogListController(LogListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    private void onAddLogButtonClick() {
        this.viewModel.createNewLog();
    }
}
