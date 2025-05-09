package ssoms;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomepageController {

    @FXML
    private TableView<Person> table;
    @FXML
    private TableColumn<Person, String> idNUm;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, String> genderColumn;
    @FXML
    private TableColumn<Person, String> birthDateColumn;

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField gender;
    @FXML
    private DatePicker bday;

    @FXML
    private Text upperlbl;
    @FXML
    private Text lowerlbl;
    @FXML
    public Button perinf;
    @FXML
    public Button orgChart;
    @FXML
    public Button visLog;
    @FXML
    public Button incidentRep;

    private ObservableList<Person> personnelList = FXCollections.observableArrayList();
    private ObservableList<Person> visitorList = FXCollections.observableArrayList();
    @FXML
    private GridPane gp1;
    @FXML
    private GridPane gp2;
    @FXML
    private GridPane gp3;
    @FXML
    private GridPane gp4;
    @FXML
    private TextField fn3;
    @FXML
    private TextField ln3;
    @FXML
    private TextField gen3;
    @FXML
    private TextField purpose3;
    @FXML
    private TableColumn<Person, String> tvfn3;
    @FXML
    private TableColumn<Person, String> tvln3;
    @FXML
    private TableColumn<Person, String> tvgen3;
    @FXML
    private TableColumn<Person, String> tvpurpose3;
    @FXML
    private TableColumn<Person, String> tvdt3;
    @FXML
    private DatePicker date3;
    @FXML
    private TableView<Person> table3;
    @FXML
    private ComboBox<String> area;
    @FXML
    private ComboBox<String> officers;
    @FXML
    private TableView<Person> table2;
    @FXML
    private TableColumn<Person, String> tvname2;
    @FXML
    private TableColumn<Person, String> desigarea;

    private ObservableList<Person> assignedOfficers = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> level;
    @FXML
    private TextArea low;
    @FXML
    private TextArea medium;
    @FXML
    private TextArea high;
    @FXML
    private ComboBox<String> officers2;
    @FXML
    private DatePicker incidentDate;
    @FXML
    private TextArea cause;

    public void initialize() {

        idNUm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        birthDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthDate()));
        table.setItems(personnelList);

        tvfn3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        tvln3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        tvgen3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        tvpurpose3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
        tvdt3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthDate()));
        table3.setItems(visitorList);

        area.setItems(FXCollections.observableArrayList("Front Gate", "Parking Lot (Outside)", "Covered Court"));
        officers.setItems(FXCollections.observableArrayList());
        tvname2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        desigarea.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
        table2.setItems(assignedOfficers);

        tvname2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        desigarea.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
        level.setItems(FXCollections.observableArrayList("Severity 1 (High)", "Severity 2 (Medium)", "Severity 3 (Low)"));
        low.setEditable(false);
        medium.setEditable(false);
        high.setEditable(false);

    }

    @FXML
    private void addPersonel(ActionEvent event) {
        String fn = firstName.getText().trim();
        String ln = lastName.getText().trim();
        String gen = gender.getText().trim();
        String bdate = (bday.getValue() != null) ? bday.getValue().toString() : "";

        if (fn.isEmpty() || ln.isEmpty() || gen.isEmpty() || bdate.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Input");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please fill in all fields before adding a visitor.");
            alert.showAndWait();
            return;

        }
        personnelList.add(new Person(fn, ln, gen, bdate));
        updateOfficersComboBox();
        firstName.clear();
        lastName.clear();
        gender.clear();
        bday.setValue(null);

    }

    @FXML
    private void handleClicks(ActionEvent event) {
        if (event.getSource() == perinf) {
            upperlbl.setText("PERSONNEL");
            lowerlbl.setText("home/personnels");
            gp1.toFront();
        } else if (event.getSource() == orgChart) {
            upperlbl.setText("EVENT HANDLER");
            lowerlbl.setText("home/eventHandler");
            gp2.toFront();
        } else if (event.getSource() == visLog) {
            upperlbl.setText("VISITOR's LOGBOOK");
            lowerlbl.setText("home/visLog");
            gp3.toFront();
        } else if (event.getSource() == incidentRep) {
            upperlbl.setText("INCIDENT REPORTS");
            lowerlbl.setText("home/inciReport");
            gp4.toFront();
        }
    }

    @FXML
    private void add3(ActionEvent event) {

        String fn = fn3.getText().trim();
        String ln = ln3.getText().trim();
        String gen = gen3.getText().trim();
        String purpose = purpose3.getText().trim();
        String date = "";
        if (date3.getValue() != null) {
            java.time.LocalDate datePart = date3.getValue();
            java.time.LocalTime timePart = java.time.LocalTime.now();
            java.time.LocalDateTime dateTime = java.time.LocalDateTime.of(datePart, timePart);
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            date = dateTime.format(formatter);
        }

        if (fn.isEmpty() || ln.isEmpty() || gen.isEmpty() || purpose.isEmpty() || date.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Input");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please fill in all fields before adding a visitor.");
            alert.showAndWait();
            return;
        }

        visitorList.add(new Person(fn, ln, gen, date, purpose));
        updateOfficersComboBox();
        fn3.clear();
        ln3.clear();
        gen3.clear();
        purpose3.clear();
        date3.setValue(null);

    }

    @FXML
    private void del3(ActionEvent event) {
        Person selected = table3.getSelectionModel().getSelectedItem();
        if (selected != null) {
            visitorList.remove(selected);
        }
    }

    @FXML
    private void confirm(ActionEvent event) {

        String selectedOfficer = officers.getSelectionModel().getSelectedItem();
        String selectedArea = area.getSelectionModel().getSelectedItem();

        if (selectedOfficer == null || selectedArea == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Selection");
            alert.setHeaderText("Selection Missing");
            alert.setContentText("Please select both an officer and an area.");
            alert.showAndWait();
            return;
        }

        String[] nameParts = selectedOfficer.split(" ", 2);
        String fn = nameParts[0];
        String ln = (nameParts.length > 1) ? nameParts[1] : "";

        for (Person p : assignedOfficers) {
            if ((p.getFirstName() + " " + p.getLastName()).equals(selectedOfficer)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Duplicate Assignment");
                alert.setHeaderText("Officer Already Assigned");
                alert.setContentText("This officer is already assigned.");
                alert.showAndWait();
                return;
            }
        }

        Person officer = new Person(fn, ln, "", "", selectedArea);
        assignedOfficers.add(officer);

        officers.getSelectionModel().clearSelection();
        area.getSelectionModel().clearSelection();

    }

    @FXML
    private void del2(ActionEvent event) {
        Person selected = table2.getSelectionModel().getSelectedItem();
        if (selected != null) {
            assignedOfficers.remove(selected);
        }

    }

    private void updateOfficersComboBox() {
        ObservableList<String> officerNames = FXCollections.observableArrayList();
        for (Person p : personnelList) {
            officerNames.add(p.getFirstName() + " " + p.getLastName());
        }

        officers.setItems(officerNames);
        if (officers2 != null) {
            officers2.setItems(officerNames);
        }
    }

    @FXML
    private void report(ActionEvent event) {

        String selectedSeverity = level.getSelectionModel().getSelectedItem();
        String selectedOfficer = officers2.getSelectionModel().getSelectedItem();
        String incidentCause = cause.getText().trim();
        String reportDate = "";
        if (incidentDate.getValue() != null) {
            java.time.LocalDate date = incidentDate.getValue();
            java.time.LocalTime time = java.time.LocalTime.now();
            java.time.LocalDateTime dateTime = java.time.LocalDateTime.of(date, time);
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            reportDate = dateTime.format(formatter);
        }

        if (selectedSeverity == null || selectedOfficer == null || reportDate.isEmpty() || incidentCause.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Fields");
            alert.setHeaderText("Incomplete Incident Report");
            alert.setContentText("Please make sure a severity level, officer, date, and cause are provided.");
            alert.showAndWait();
            return;
        }

        String reportEntry = "Officer: " + selectedOfficer
                + "\nDate: " + reportDate
                + "\nCause: " + incidentCause + "\n\n";

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
        }

        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Incident Report Added");
        success.setHeaderText("Success");
        success.setContentText("Incident information has been recorded.");
        success.showAndWait();

        level.getSelectionModel().clearSelection();
        officers2.getSelectionModel().clearSelection();
        incidentDate.setValue(null);
        cause.clear();
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
                    
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
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

    public static class Person {

        private final SimpleStringProperty id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty birthDate;
        private final SimpleStringProperty purpose;

        public Person(String firstName, String lastName, String gender, String birthDate) {
            this(firstName, lastName, gender, birthDate, "");
        }

        public Person(String firstName, String lastName, String gender, String birthDate, String purpose) {
            this.id = new SimpleStringProperty(String.valueOf((int) (Math.random() * 9000 + 1000)));
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.gender = new SimpleStringProperty(gender);
            this.birthDate = new SimpleStringProperty(birthDate);
            this.purpose = new SimpleStringProperty(purpose);
        }

        public String getId() {
            return id.get();
        }

        public String getFirstName() {
            return firstName.get();
        }

        public String getLastName() {
            return lastName.get();
        }

        public String getGender() {
            return gender.get();
        }

        public String getBirthDate() {
            return birthDate.get();
        }

        public String getPurpose() {
            return purpose.get();
        }
    }

}
