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

    public ObservableList<Person> personnelList = Personnels.getInstance().getPersonnelList();


    @FXML
    private GridPane gp1;
    @FXML
    private GridPane gp2;

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
    public void initialize() {

        idNUm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        birthDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthDate()));
        table.setItems(personnelList);

        area.setItems(FXCollections.observableArrayList("Front Gate", "Parking Lot (Outside)", "Covered Court"));
        officers.setItems(FXCollections.observableArrayList());
        tvname2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        desigarea.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
        table2.setItems(assignedOfficers);

        tvname2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        desigarea.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
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
