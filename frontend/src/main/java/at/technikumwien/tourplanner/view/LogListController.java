package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.LogItem;
import at.technikumwien.tourplanner.viewmodel.LogListViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class LogListController {
    @FXML
    private TableView<LogItem> logTable;
    @FXML
    private TableColumn<LogItem, String> dateColumn;
    @FXML
    private TableColumn<LogItem, String> difficultyColumn;
    @FXML
    private TableColumn<LogItem, String> commentColumn;
    @FXML
    private TableColumn<LogItem, String> totalTimeColumn;
    @FXML
    private TableColumn<LogItem, String> totalDistanceColumn;
    @FXML
    private TableColumn<LogItem, String> ratingColumn;
    @FXML
    private TextField searchField;

    private final LogListViewModel logListViewModel;

    public LogListController(LogListViewModel logListViewModel) {
        this.logListViewModel = logListViewModel;
    }

    @FXML
    public void initialize() {
        logTable.setItems(logListViewModel.getFilteredLogs());

        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        difficultyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDifficulty())));
        commentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComment()));
        totalTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTotalTime()));
        totalDistanceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTotalDistance()));
        ratingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRating()));

        logTable.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                logListViewModel.selectedLogProperty().set(newItem);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            logListViewModel.filterLogs(newValue);
        });
    }

    @FXML
    private void onCreateLogButtonClick() {
        this.logListViewModel.createNewLog();
    }

    @FXML
    public void onEditLogButtonClick(ActionEvent actionEvent) {
        if (logListViewModel.selectedLogProperty().get() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("NO Log Selection");
            alert.setContentText("Please select a log to edit!");
            alert.showAndWait();
            return;
        }

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

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Log");
        confirmDialog.setHeaderText("Delete Log");
        confirmDialog.setContentText("Are you sure you want to delete this log? This action cannot be undone.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logListViewModel.deleteLog();
        }
    }
}