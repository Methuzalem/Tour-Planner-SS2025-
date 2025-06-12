package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.viewmodel.EditLogViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class EditLogController {
    private EditLogViewModel viewModel;

    public EditLogController(EditLogViewModel viewModel) {this.viewModel = viewModel;}

    @FXML private DatePicker datePicker;
    @FXML private TextArea commentTextArea;
    @FXML private Slider difficultySlider;
    @FXML private TextField totalTimeTextField;
    @FXML private TextField distanceTextField;
    @FXML private ComboBox<String> ratingComboBox;

    @FXML
    public void initialize() {
        // Populate transport type options
        ratingComboBox.getItems().addAll("5 - best", "4 - good", "3 - normal", "2 - bad", "1 - really bad");

        // Bind all fields to the view model properties
        datePicker.valueProperty().bindBidirectional(viewModel.dateProperty());
        Bindings.bindBidirectional(commentTextArea.textProperty(), viewModel.commentProperty());
        difficultySlider.valueProperty().bindBidirectional(viewModel.difficultyProperty());
        Bindings.bindBidirectional(totalTimeTextField.textProperty(), viewModel.totalTimeProperty());
        Bindings.bindBidirectional(distanceTextField.textProperty(), viewModel.totalDistanceProperty());
        ratingComboBox.valueProperty().bindBidirectional(viewModel.ratingProperty());
    }

    @FXML
    public void onCancelLogButtonClick() {
            viewModel.cancelEditLog();
    }

    @FXML
    public void onSaveLogButtonClick() {
        viewModel.createNewLog();
    }

}
