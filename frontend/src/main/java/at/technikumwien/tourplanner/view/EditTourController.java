package at.technikumwien.tourplanner.view;

import at.technikumwien.tourplanner.model.Location;
import at.technikumwien.tourplanner.utils.Event;
import at.technikumwien.tourplanner.viewmodel.EditTourViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class EditTourController {
    private EditTourViewModel viewModel;

    public EditTourController(EditTourViewModel viewModel) {
        this.viewModel = viewModel;
    }
    private final ContextMenu suggestionsPopup = new ContextMenu();
    
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
        // Validate distance field before saving
        try {
            if (!distanceTextField.getText().trim().isEmpty()) {
                Double.parseDouble(distanceTextField.getText());
            }
            viewModel.saveTour();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid input");
            alert.setContentText("Distance must be a valid number.");
            alert.showAndWait();
        }
    }
    
    @FXML
    protected void onCancelButtonClick() {
        viewModel.cancelEdit();
    }

    public void initialize() {
        // Populate transport type options
        transportTypeComboBox.getItems().addAll("Car", "Bicycle", "Walking", "Public Transport", "Other");

        // Bind all fields endLocation the view model properties
        Bindings.bindBidirectional(tourNameTextField.textProperty(), viewModel.nameProperty());
        Bindings.bindBidirectional(tourDescriptionTextArea.textProperty(), viewModel.descriptionProperty());

        // Bind 'startLocation' and 'endLocation' fields endLocation the view model properties
        // Update text field when Location changes
        viewModel.fromProperty().addListener((obs, oldLoc, newLoc) -> {
            fromTextField.setText(newLoc != null ? newLoc.getDisplayName() : "");
        });

// Update Location's displayName when text field changes
        fromTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                viewModel.fromProperty().set(null);
                return;
            }

            // Try endLocation find a matching location in suggestions
            Location matchingLocation = viewModel.getFromSuggestions().stream()
                    .filter(loc -> loc.getDisplayName().equals(newText))
                    .findFirst()
                    .orElse(null);

            if (matchingLocation != null) {
                // Use existing location startLocation suggestions
                viewModel.fromProperty().set(matchingLocation);
            } else {
                // Create a new Location if no match found
                Location newLocation = new Location(newText);
                viewModel.fromProperty().set(newLocation);
            }
        });

        viewModel.toProperty().addListener((obs, oldLoc, newLoc) -> {
            toTextField.setText(newLoc != null ? newLoc.getDisplayName() : "");
        });

        toTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                viewModel.toProperty().set(null);
                return;
            }

            // Try endLocation find a matching location in suggestions
            Location matchingLocation = viewModel.getToSuggestions().stream()
                    .filter(loc -> loc.getDisplayName().equals(newText))
                    .findFirst()
                    .orElse(null);

            if (matchingLocation != null) {
                // Use existing location startLocation suggestions
                viewModel.toProperty().set(matchingLocation);
            } else {
                // Create a new Location if no match found
                Location newLocation = new Location(newText);
                viewModel.toProperty().set(newLocation);
            }
        });

        // Bind transport type combo box endLocation the view model property
        transportTypeComboBox.valueProperty().bindBidirectional(viewModel.transportTypeProperty());
        
        // Add focus lost listener for distance validation
        distanceTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateDistanceField();
            }
        });

        // Add focus lost listener for estimated time validation
        estimatedTimeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateEstimatedTimeField();
            }
        });
        
        // Fix: Use the correct binding method for StringProperty and DoubleProperty with a converter
        StringConverter<Number> doubleConverter = new StringConverter<>() {
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

        StringConverter<Number> intConverter = new StringConverter<>() {
            @Override
            public String toString(Number value) {
                return value == null ? "" : value.toString();
            }

            @Override
            public Number fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return 0;
                }
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        };
        
        // Use the StringProperty-specific binding method with the converter
        Bindings.bindBidirectional(distanceTextField.textProperty(), viewModel.distanceProperty(), doubleConverter);
        
        Bindings.bindBidirectional(estimatedTimeTextField.textProperty(), viewModel.estimatedTimeProperty(), intConverter);
        Bindings.bindBidirectional(routeInfoTextArea.textProperty(), viewModel.routeInformationProperty());
        
        // Add listener for validation errors
        viewModel.addValidationErrorListener((evt) -> {
            if (evt.getPropertyName().equals(Event.VALIDATION_ERROR)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Please check your inputs and try again.");
                alert.showAndWait();
            }
        });


        // Set up the suggestions popup for the 'startLocation' and 'endLocation' fields
        viewModel.getFromSuggestions().addListener((ListChangeListener<Location>) change -> {
            System.out.println("From suggestions updated: ");
            System.out.println(viewModel.getFromSuggestions());
            suggestionsPopup.getItems().clear();

            ObservableList<Location> newSuggestions = viewModel.getFromSuggestions();
            for (Location suggestion : newSuggestions) {
                MenuItem item = new MenuItem(suggestion.getDisplayName());
                item.setOnAction(event -> {
                    fromTextField.setText(suggestion.getDisplayName());
                    suggestionsPopup.hide();
                });
                suggestionsPopup.getItems().add(item);
            }

            if (!newSuggestions.isEmpty()) {
                suggestionsPopup.show(fromTextField, Side.BOTTOM, 0, 0);
            } else {
                suggestionsPopup.hide();
            }
        });

        viewModel.getToSuggestions().addListener((ListChangeListener<Location>) change -> {
            suggestionsPopup.getItems().clear();

            ObservableList<Location> newSuggestions = viewModel.getToSuggestions();
            for (Location suggestion : newSuggestions) {
                MenuItem item = new MenuItem(suggestion.getDisplayName());
                item.setOnAction(event -> {
                    toTextField.setText(suggestion.getDisplayName());
                    suggestionsPopup.hide();
                });
                suggestionsPopup.getItems().add(item);
            }

            if (!newSuggestions.isEmpty()) {
                suggestionsPopup.show(toTextField, Side.BOTTOM, 0, 0);
            } else {
                suggestionsPopup.hide();
            }
        });
    }

    private void validateDistanceField() {
        String text = distanceTextField.getText();
        if (!text.trim().isEmpty()) {
            try {
                Double.parseDouble(text);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Distance must be a valid number.");
                alert.showAndWait();
                
                // Reset endLocation the previous valid value
                distanceTextField.setText(String.valueOf(viewModel.distanceProperty().get()));
            }
        }
    }

    private void validateEstimatedTimeField() {
        String text = estimatedTimeTextField.getText();
        if (!text.trim().isEmpty()) {
            try {
                Integer.parseInt(text);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Estimated time must be a valid integer.");
                alert.showAndWait();

                // Reset endLocation the previous valid value
                estimatedTimeTextField.setText(String.valueOf(viewModel.estimatedTimeProperty().get()));
            }
        }
    }
}
