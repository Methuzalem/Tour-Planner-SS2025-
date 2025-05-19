package at.technikumwien.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import java.time.format.DateTimeFormatter;

public class EditLogController {
    @FXML
    public DatePicker datePicker;

    String selectedDateString = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    public void onCancelLogButtonClick(ActionEvent actionEvent) {
    }

    public void onSaveLogButtonClick(ActionEvent actionEvent) {
    }
}
