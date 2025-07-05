package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.viewmodel.EditLogViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import at.technikumwien.tourplanner.utils.RatingOption;
import javafx.util.converter.IntegerStringConverter;


public class EditLogController {
    private EditLogViewModel viewModel;

    public EditLogController(EditLogViewModel viewModel) {this.viewModel = viewModel;}

    @FXML private DatePicker datePicker;
    @FXML private TextArea commentTextArea;
    @FXML private Slider difficultySlider;
    @FXML private TextField totalTimeTextField;
    @FXML private ComboBox<RatingOption> ratingComboBox;

    @FXML
    public void initialize() {
        // Populate transport type options
        ratingComboBox.getItems().addAll(
                new RatingOption("5 - Sehr Gut", 5),
                new RatingOption("4 - Gut", 4),
                new RatingOption("3 - Mittel", 3),
                new RatingOption("2 - Schlecht", 2),
                new RatingOption("1 - Sehr Schlecht", 1)
        );

        TextFormatter<String> integerOnlyFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null; // only numbers allowed
        });

        totalTimeTextField.setTextFormatter(integerOnlyFormatter);

        // Bind all fields endLocation the view model properties
        datePicker.valueProperty().bindBidirectional(viewModel.dateProperty());
        Bindings.bindBidirectional(commentTextArea.textProperty(), viewModel.commentProperty());
        difficultySlider.valueProperty().bindBidirectional(viewModel.difficultyProperty());
        Bindings.bindBidirectional(
                totalTimeTextField.textProperty(),
                (Property<Integer>) (Object) viewModel.totalTimeProperty(),
                new IntegerStringConverter()
        );
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
