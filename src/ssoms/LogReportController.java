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
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private TextArea low;
    @FXML
    private TextArea medium;
    @FXML
    private TextArea high;
    @FXML
    private ImageView img;

    private File attachedImageFile = null; // ✅ Track attached image file

    @FXML
    private void initialize() {
        // Initialize visitor log table
        tvfn3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        tvln3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        tvgen3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        tvpurpose3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
        tvdt3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthDate()));
        table3.setItems(visitorList);

        // Incident severity levels — ensure this is called!
        level.setItems(FXCollections.observableArrayList(
                "Severity 1 (High)",
                "Severity 2 (Medium)",
                "Severity 3 (Low)"
        ));

        // Officer names — populate from Personnels singleton
        ObservableList<String> officerNames = FXCollections.observableArrayList();
        for (HomepageController.Person p : Personnels.getInstance().getPersonnelList()) {
            officerNames.add(p.getFirstName() + " " + p.getLastName());
        }
        officers2.setItems(officerNames);

        // Listen for personnel changes to update officers ComboBox dynamically
        Personnels.getInstance().getPersonnelList().addListener((javafx.collections.ListChangeListener.Change<? extends HomepageController.Person> change) -> {
            officerNames.clear();
            for (HomepageController.Person p : Personnels.getInstance().getPersonnelList()) {
                officerNames.add(p.getFirstName() + " " + p.getLastName());
            }
            officers2.setItems(officerNames);
        });
        
        

        // Lock incident log text areas to read-only
        low.setEditable(false);
        medium.setEditable(false);
        high.setEditable(false);

        // Set initial pane visibility
        gp3.setVisible(true);
        gp4.setVisible(false);

        officers2.setPromptText("Select Officer");
        level.setPromptText("Select Severity Level");
      

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
                + "\n\n";

        switch (selectedSeverity) {
            case "Severity 3 (Low)":
                low.appendText(reportEntry);
                break;
            case "Severity 2 (Medium)":
                medium.appendText(reportEntry);
                break;
            case "Severity 1 (High)":
                high.appendText(reportEntry);
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
        img.setImage(null);                  // ✅ Clear image preview
        attachedImageFile = null;            // ✅ Reset image file reference
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
        alert.setTitle("Notification");
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
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                img.setImage(image);
                attachedImageFile = selectedFile;
                showAlert(Alert.AlertType.INFORMATION, "Image Attached", "Incident photo has been successfully attached.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Image Load Error", "Unable to load selected image.");
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "No Image Selected", "No image was chosen.");
        }
    }

    public void setPersonnelList(ObservableList<HomepageController.Person> personnelList) {
        ObservableList<String> officerNames = FXCollections.observableArrayList();
        for (HomepageController.Person p : personnelList) {
            officerNames.add(p.getFirstName() + " " + p.getLastName());
        }

        personnelList.addListener((javafx.collections.ListChangeListener.Change<? extends HomepageController.Person> change) -> {
            officerNames.clear();
            for (HomepageController.Person p : personnelList) {
                officerNames.add(p.getFirstName() + " " + p.getLastName());
            }
        });

        officers2.setItems(officerNames);
    }
}
