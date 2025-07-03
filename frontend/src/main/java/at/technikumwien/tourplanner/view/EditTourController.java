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
    @FXML private TextArea routeInfoTextArea;
    
    @FXML
    protected void onSaveButtonClick() {
        // Validate fields before saving
        if (!validateNameField() || !validateDescriptionField() || !validateFromField() || !validateToField() || !validateTransportTypeField()) {
            return; // If any validation fails, do not proceed with saving
        }

        viewModel.saveTour();
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

            Location currentFromLocation = viewModel.fromProperty().get();
            if (matchingLocation != null) {
                // Use existing location startLocation suggestions
                viewModel.fromProperty().set(matchingLocation);
            } else if (currentFromLocation == null || (currentFromLocation.getLatitude() == 0 && currentFromLocation.getLongitude() == 0)) {
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

            Location currentToLocation = viewModel.toProperty().get();
            if (matchingLocation != null) {
                // Use existing location startLocation suggestions
                viewModel.toProperty().set(matchingLocation);
            } else if(currentToLocation == null || (currentToLocation.getLatitude() == 0 && currentToLocation.getLongitude() == 0)) {
                // Create a new Location if no match found and current location is not set or invalid
                Location newLocation = new Location(newText);
                viewModel.toProperty().set(newLocation);
            }
        });

        // Bind transport type combo box endLocation the view model property
        transportTypeComboBox.valueProperty().bindBidirectional(viewModel.transportTypeProperty());

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

    // validate name field
    private boolean validateNameField() {
        if(tourNameTextField.getText().isEmpty()) {
            showValidationError("Tour name cannot be empty.");
            return false;
        }
        return true;
    }

    // validate description field
    private boolean validateDescriptionField() {
        if(tourDescriptionTextArea.getText().isEmpty()) {
            showValidationError("Tour description cannot be empty.");
            return false;
        }
        return true;
    }

    // validate from field
    private boolean validateFromField() {
        if(fromTextField.getText().isEmpty()) {
            showValidationError("Start location cannot be empty.");
            return false;
        }

        // check if the location has latitude and longitude
        Location fromLocation = viewModel.fromProperty().get();
        if (fromLocation == null || fromLocation.getLatitude() == 0 || fromLocation.getLongitude() == 0) {
            showValidationError("Please select a start location from the suggestions.");
            return false;
        }

        return true;
    }

    // validate to field
    private boolean validateToField() {
        if(toTextField.getText().isEmpty()) {
            showValidationError("End location cannot be empty.");
            return false;
        }

        // check if the location has latitude and longitude
        Location toLocation = viewModel.toProperty().get();
        if (toLocation == null || toLocation.getLatitude() == 0 || toLocation.getLongitude() == 0) {
            showValidationError("Please select an end location from the suggestions.");
            return false;
        }
        return true;
    }

    // validate transport type field
    private boolean validateTransportTypeField() {
        if (transportTypeComboBox.getValue() == null || transportTypeComboBox.getValue().isEmpty()) {
            showValidationError("Transport type cannot be empty.");
            return false;
        }
        return true;
    }


    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
