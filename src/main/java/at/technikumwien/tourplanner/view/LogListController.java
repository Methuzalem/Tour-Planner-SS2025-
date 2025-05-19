package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.viewmodel.LogListViewModel;
import at.technikumwien.tourplanner.viewmodel.TourListViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class LogListController {
    @FXML
    private ListView<LogItem> logList;

    private final LogListViewModel viewLog;
    private final TourListViewModel viewTour;

    public LogListController(LogListViewModel viewLog,TourListViewModel viewTour) {
        this.viewLog = viewLog;
        this.viewTour = viewTour;
    }

    @FXML
    public void initialize() {
        // Bind logList Items filtered
        logList.setItems(viewLog.getFilteredLogs());

        // show Items row per row
        logList.setCellFactory(listView -> new ListCell<LogItem>() {
            @Override
            protected void updateItem(LogItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Date: " + item.getDate() + " | Difficulty: " + item.getDifficulty() + " | Note: " + item.getNote());
                }
            }
        });

    }

    @FXML
    private void onAddLogButtonClick() {
        this.viewLog.createNewLog();
    }
}
