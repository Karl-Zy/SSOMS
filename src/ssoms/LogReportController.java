package ssoms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class LogReportController {

    @FXML
    private TextField fn3, ln3, gen3, purpose3;
    @FXML
    private DatePicker date3;
    @FXML
    private TableView<HomepageController.Person> table3;
    @FXML
    private TableColumn<HomepageController.Person, String> tvfn3, tvln3, tvgen3, tvpurpose3, tvdt3;

    private final ObservableList<HomepageController.Person> visitorList = FXCollections.observableArrayList();

    @FXML
    private Button visLog;
    @FXML
    private Button incidentRep;
    @FXML
    private Text upperlbl;
    @FXML
    private Text lowerlbl;

    @FXML
    private GridPane gp3;
    @FXML
    private GridPane gp4;

    @FXML
    private ComboBox<String> officers2;
    @FXML
    private DatePicker incidentDate;
    @FXML
    private ComboBox<String> level;

    @FXML
    private TextArea cause;
    @FXML
    private VBox low;
    @FXML
    private VBox medium;
    @FXML
    private VBox high;
    @FXML
    private ImageView img;

    private File attachedImageFile = null; // Track attached image file

    // Changed from Label to VBox for selected incident report container
    private VBox selectedIncidentBox = null;

    @FXML
    private void initialize() {
        // Initialize visitor log table
        tvfn3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        tvln3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        tvgen3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        tvpurpose3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
        tvdt3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthDate()));
        table3.setItems(visitorList);

        // Incident severity levels
        level.setItems(FXCollections.observableArrayList(
                "Severity 1 (High)",
                "Severity 2 (Medium)",
                "Severity 3 (Low)"
        ));

        // Populate officers ComboBox
        ObservableList<String> officerNames = FXCollections.observableArrayList();
        for (HomepageController.Person p : Personnels.getInstance().getPersonnelList()) {
            officerNames.add(p.getFirstName() + " " + p.getLastName());
        }
        officers2.setItems(officerNames);

        // Update officers ComboBox dynamically if personnel changes
        Personnels.getInstance().getPersonnelList().addListener((javafx.collections.ListChangeListener.Change<? extends HomepageController.Person> change) -> {
            officerNames.clear();
            for (HomepageController.Person p : Personnels.getInstance().getPersonnelList()) {
                officerNames.add(p.getFirstName() + " " + p.getLastName());
            }
            officers2.setItems(officerNames);
        });

        // Set initial pane visibility
        gp3.setVisible(true);
        gp4.setVisible(false);

        officers2.setPromptText("Select Officer");
        level.setPromptText("Select Severity Level");

        addHoverEffect(low);
        addHoverEffect(medium);
        addHoverEffect(high);
    }

    private void addHoverEffect(VBox box) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), box);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), box);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        box.setOnMouseEntered(e -> {
            scaleDown.stop();
            scaleUp.playFromStart();
        });

        box.setOnMouseExited(e -> {
            scaleUp.stop();
            scaleDown.playFromStart();
        });
    }

    @FXML
    private void add3(ActionEvent event) {
        String fn = fn3.getText().trim();
        String ln = ln3.getText().trim();
        String gen = gen3.getText().trim();
        String purpose = purpose3.getText().trim();
        String date = "";

        if (date3.getValue() != null) {
            LocalDateTime dateTime = LocalDateTime.of(date3.getValue(), java.time.LocalTime.now());
            date = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if (fn.isEmpty() || ln.isEmpty() || gen.isEmpty() || purpose.isEmpty() || date.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Input", "Please fill in all fields before adding a visitor.");
            return;
        }

        HomepageController.Person newVisitor = new HomepageController.Person(fn, ln, gen, date, purpose);
        visitorList.add(newVisitor);

        File file = new File("visitor_log.txt");

        try (FileWriter writer = new FileWriter(file, true)) {
            if (file.length() == 0) {
                writer.write(String.format("%-20s%-20s%-10s%-25s%-20s\n",
                        "First Name", "Last Name", "Gender", "Purpose", "Date"));
            }

            writer.write(String.format("%-20s%-20s%-10s%-25s%-20s\n",
                    fn, ln, gen, purpose.replace("\n", " "), date));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not save visitor information to file.");
            e.printStackTrace();
        }

        fn3.clear();
        ln3.clear();
        gen3.clear();
        purpose3.clear();
        date3.setValue(null);
    }

    @FXML
    private void del3(ActionEvent event) {
        HomepageController.Person selected = table3.getSelectionModel().getSelectedItem();
        if (selected != null) {
            visitorList.remove(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a visitor to delete.");
        }
    }

    @FXML
    private void report(ActionEvent event) {
        String selectedSeverity = level.getSelectionModel().getSelectedItem();
        String selectedOfficer = officers2.getSelectionModel().getSelectedItem();
        String incidentCause = cause.getText().trim();
        String reportDate = "";

        if (incidentDate.getValue() != null) {
            LocalDateTime dateTime = LocalDateTime.of(incidentDate.getValue(), java.time.LocalTime.now());
            reportDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if (selectedSeverity == null || selectedOfficer == null || reportDate.isEmpty() || incidentCause.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please make sure a severity level, officer, date, and cause are provided.");
            return;
        }

        String imagePath = (attachedImageFile != null) ? attachedImageFile.getAbsolutePath() : "No Image Attached";

        String reportEntry = "Officer: " + selectedOfficer
                + "\nDate: " + reportDate
                + "\nCause: " + incidentCause
                + "\nImage: " + imagePath
                + "\n";

        switch (selectedSeverity) {
            case "Severity 3 (Low)":
            case "Severity 2 (Medium)":
            case "Severity 1 (High)":
                addIncidentReport(selectedSeverity, reportEntry);
                break;
            default:
                showAlert(Alert.AlertType.WARNING, "Unknown Severity", "Selected severity level is not recognized.");
                return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Incident Report Added", "Incident information has been recorded.");

        File file = new File("incident_report_log.txt");

        try (FileWriter writer = new FileWriter(file, true)) {
            if (file.length() == 0) {
                writer.write(String.format("%-25s%-20s%-40s%-20s%-50s\n",
                        "Officer Name", "Severity Level", "Incident Cause", "Date", "Image Path"));
            }

            writer.write(String.format("%-25s%-20s%-40s%-20s\n",
                    selectedOfficer, selectedSeverity, incidentCause.replace("\n", " "), reportDate));

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not save incident report to file.");
            e.printStackTrace();
        }

        level.getSelectionModel().clearSelection();
        officers2.getSelectionModel().clearSelection();
        incidentDate.setValue(null);
        cause.clear();
        img.setImage(null);                  // Clear image preview
        attachedImageFile = null;            // Reset image file reference
    }

    // Helper method to add incident report VBox to corresponding AnchorPane
    private void addIncidentReport(String severity, String text) {
        VBox reportBox = new VBox(5);
        reportBox.setPickOnBounds(true);
        reportBox.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
        reportBox.setMaxWidth(350);

        Label reportLabel = new Label(text);
        reportLabel.setWrapText(true);

        reportBox.getChildren().add(reportLabel);

        // Select the entire VBox when clicked
        reportBox.setOnMouseClicked(e -> {
            if (selectedIncidentBox != null) {
                selectedIncidentBox.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
            }
            selectedIncidentBox = reportBox;
            reportBox.setStyle("-fx-padding: 5; -fx-background-color: lightblue; -fx-border-color: blue;");
        });

        // Initialize expanded state as false if you want to use it later
        reportBox.setUserData(false);

        AnchorPane container = null;
        switch (severity) {
            case "Severity 3 (Low)":
                ScrollPane lowScroll = (ScrollPane) low.getChildren().get(0);
                container = (AnchorPane) lowScroll.getContent();
                break;
            case "Severity 2 (Medium)":
                ScrollPane mediumScroll = (ScrollPane) medium.getChildren().get(0);
                container = (AnchorPane) mediumScroll.getContent();
                break;
            case "Severity 1 (High)":
                ScrollPane highScroll = (ScrollPane) high.getChildren().get(0);
                container = (AnchorPane) highScroll.getContent();
                break;
        }

        if (container != null) {
            container.getChildren().add(reportBox);
        }
    }

    @FXML
    private void deleteIncident(ActionEvent event) {
        if (selectedIncidentBox == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an incident report to delete.");
            return;
        }

        boolean removed = false;
        for (VBox vbox : new VBox[]{low, medium, high}) {
            ScrollPane sp = (ScrollPane) vbox.getChildren().get(0);
            AnchorPane pane = (AnchorPane) sp.getContent();
            if (pane.getChildren().remove(selectedIncidentBox)) {
                removed = true;
                break;
            }
        }

        if (removed) {
            selectedIncidentBox = null;
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Selected incident report has been deleted.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the selected incident report.");
        }
    }

    @FXML
    private void handleClicks(ActionEvent event) {
        if (event.getSource() == visLog) {
            gp3.setVisible(true);
            gp4.setVisible(false);
            upperlbl.setText("VISITOR's LOGBOOK");
            lowerlbl.setText("intro/visitor's");
        } else if (event.getSource() == incidentRep) {
            gp3.setVisible(false);
            gp4.setVisible(true);
            upperlbl.setText("INCIDENT REPORT");
            lowerlbl.setText("intro/inciReport");
        }
    }

    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void logout(ActionEvent event) {
        Alert confirmLogout = new Alert(Alert.AlertType.CONFIRMATION);
        confirmLogout.setTitle("Confirm Logout");
        confirmLogout.setHeaderText("Are you sure you want to log out?");
        confirmLogout.setContentText("Click 'Yes' to log out or 'No' to stay on this page.");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        confirmLogout.getButtonTypes().setAll(yesButton, noButton);

        confirmLogout.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Intro.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Login");
                    stage.show();
                    javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Logout Failed");
                    errorAlert.setContentText("Unable to load the login screen.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    @FXML
    private void attachimg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Incident Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            attachedImageFile = file;
            Image image = new Image(file.toURI().toString());
            img.setImage(image);
        }
    }
}
