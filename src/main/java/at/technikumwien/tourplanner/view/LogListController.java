package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.viewmodel.LogListViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.Optional;

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

    @FXML
    public void onDeleteLogButtonClick(ActionEvent actionEvent) {
        if (logListViewModel.selectedLogProperty().get() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Log Selection");
            alert.setContentText("Please select a log to delete it");
            alert.showAndWait();
            return;
        }

        // Show a confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Log");
        confirmDialog.setHeaderText("Delete Log");
        confirmDialog.setContentText("Are you sure you want to delete this log? This action cannot be undone.");

        // Handle the user's response
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed, proceed with deletion
            logListViewModel.deleteLog();
        }
    }
}
