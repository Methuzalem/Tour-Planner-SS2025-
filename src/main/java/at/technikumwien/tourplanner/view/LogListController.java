package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.viewmodel.LogListViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class LogListController {
    @FXML
    private ListView<LogItem> logList;

    private final LogListViewModel logListViewModel;


    public LogListController(LogListViewModel logListViewModel) {
        this.logListViewModel = logListViewModel;
    }

    @FXML
    public void initialize() {
        // Bind logList Items filtered
        logList.setItems(logListViewModel.getFilteredLogs());

        // show Items row per row
        logList.setCellFactory(listView -> new ListCell<LogItem>() {
            @Override
            protected void updateItem(LogItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Date: " + item.getDate() + " | Difficulty: " + item.getDifficulty() + " | Rating: " + item.getRating() + " | Time: " + item.getTotalTime() + " | Distance: " + item.getTotalDistance() + " | Comment: " + item.getComment());
                }
            }
        });

        //get selected item
        logList.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                logListViewModel.selectedLogProperty().set(newItem);
            }
        });
    }

    @FXML
    private void onCreateLogButtonClick() {
        this.logListViewModel.createNewLog();
    }

    @FXML
    public void onEditLogButtonClick(ActionEvent actionEvent) {
        this.logListViewModel.editLog();
    }

    public void onDeleteLogButtonClick(ActionEvent actionEvent) {
    }
}
