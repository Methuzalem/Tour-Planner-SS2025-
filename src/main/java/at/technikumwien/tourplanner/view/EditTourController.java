package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.viewmodel.EditTourViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class EditTourController {
    private EditTourViewModel viewModel;

    public EditTourController(EditTourViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @FXML private TextField tourNameTextField;
    @FXML private TextArea tourDescriptionTextArea;
    @FXML private TextField fromTextField;
    @FXML private TextField toTextField;
    @FXML private ComboBox<String> transportTypeComboBox;
    @FXML private TextField distanceTextField;
    @FXML private TextField estimatedTimeTextField;
    @FXML private TextArea routeInfoTextArea;
    
    @FXML
    protected void onSaveButtonClick() {
        // Create the new tour
        viewModel.createNewTour();
    }
    
    @FXML
    protected void onCancelButtonClick() {
        viewModel.cancelEdit();
    }

    public void initialize() {
        // Populate transport type options
        transportTypeComboBox.getItems().addAll("Car", "Bicycle", "Walking", "Public Transport", "Other");
        
        // Bind all fields to the view model properties
        Bindings.bindBidirectional(tourNameTextField.textProperty(), viewModel.nameProperty());
        Bindings.bindBidirectional(tourDescriptionTextArea.textProperty(), viewModel.descriptionProperty());
        Bindings.bindBidirectional(fromTextField.textProperty(), viewModel.fromProperty());
        Bindings.bindBidirectional(toTextField.textProperty(), viewModel.toProperty());
        transportTypeComboBox.valueProperty().bindBidirectional(viewModel.transportTypeProperty());
        
        // Fix: Use the correct binding method for StringProperty and DoubleProperty with a converter
        StringConverter<Number> converter = new StringConverter<>() {
            @Override
            public String toString(Number value) {
                return value == null ? "" : value.toString();
            }

            @Override
            public Number fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return 0.0;
                }
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        };
        
        // Use the StringProperty-specific binding method with the converter
        Bindings.bindBidirectional(distanceTextField.textProperty(), viewModel.distanceProperty(), converter);
        
        Bindings.bindBidirectional(estimatedTimeTextField.textProperty(), viewModel.estimatedTimeProperty());
        Bindings.bindBidirectional(routeInfoTextArea.textProperty(), viewModel.routeInformationProperty());
    }
}
