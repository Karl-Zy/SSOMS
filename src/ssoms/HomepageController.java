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
    public Button visLog;
    public Button incidentRep;

    public ObservableList<Person> personnelList = Personnels.getInstance().getPersonnelList();

    @FXML
    private GridPane gp1;
    @FXML
    private GridPane gp2;

    @FXML
    private ComboBox<String> area;
    @FXML
    private ComboBox<String> event;
    @FXML
    private ComboBox<String> officers;
    @FXML
    private TableView<Person> table2;
    @FXML
    private TableColumn<Person, String> tvname2;
    @FXML
    private TableColumn<Person, String> desigarea;
    @FXML
    private TableColumn<Person, String> eventtb;

    private ObservableList<Person> assignedOfficers = FXCollections.observableArrayList();
    @FXML
    private TextField otherEvents;
    @FXML
    private TextField otherArea;

    public void initialize() {

        idNUm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        birthDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthDate()));
        table.setItems(personnelList);

        area.setItems(FXCollections.observableArrayList("Front Gate", "Parking Lot (Outside)", "Covered Court"));
        area.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                otherArea.clear();
                otherArea.setDisable(true);
            } else {
                otherArea.setDisable(false);
            }
        });

        // Enable other text fields initially
        otherEvents.setDisable(false);
        otherArea.setDisable(false);
        event.setItems(FXCollections.observableArrayList("Pasiklaban", "Foundation"));
        event.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                otherEvents.clear();
                otherEvents.setDisable(true);
            } else {
                otherEvents.setDisable(false);
            }
        });

        // If you want the otherEvents enabled when nothing is selected initially
        otherEvents.setDisable(false);
        officers.setItems(FXCollections.observableArrayList());

        tvname2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        desigarea.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
        eventtb.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent()));
        table2.setItems(assignedOfficers);
    }

    @FXML
    private void addPersonel(ActionEvent event) {
        String fn = firstName.getText().trim();
        String ln = lastName.getText().trim();
        String gen = gender.getText().trim();
        String bdate = (bday.getValue() != null) ? bday.getValue().toString() : "";

        if (fn.isEmpty() || ln.isEmpty() || gen.isEmpty() || bdate.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Input", "Please fill in all fields before adding a visitor.");
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
    private void confirm(ActionEvent eventAction) {
        String selectedOfficer = officers.getSelectionModel().getSelectedItem();
        String selectedArea = area.getSelectionModel().getSelectedItem();
        String otherAreaText = otherArea.getText().trim();
        String selectedEvent = event.getSelectionModel().getSelectedItem();
        String otherEventText = otherEvents.getText().trim();

        if (selectedOfficer == null) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Selection", "Please select an officer.");
            return;
        }

        String finalArea = (selectedArea != null && !selectedArea.isEmpty()) ? selectedArea
                : (!otherAreaText.isEmpty() ? otherAreaText : null);

        String finalEvent = (selectedEvent != null && !selectedEvent.isEmpty()) ? selectedEvent
                : (!otherEventText.isEmpty() ? otherEventText : null);

        if (finalArea == null || finalEvent == null) {
            showAlert(Alert.AlertType.WARNING, "Incomplete Selection", "Please select or enter both an area and an event.");
            return;
        }

        String[] nameParts = selectedOfficer.split(" ", 2);
        String fn = nameParts[0];
        String ln = (nameParts.length > 1) ? nameParts[1] : "";

        for (Person p : assignedOfficers) {
            if ((p.getFirstName() + " " + p.getLastName()).equals(selectedOfficer)) {
                showAlert(Alert.AlertType.INFORMATION, "Duplicate Assignment", "This officer is already assigned.");
                return;
            }
        }

        Person officer = new Person(fn, ln, "", "", finalArea, finalEvent);
        assignedOfficers.add(officer);

        officers.getSelectionModel().clearSelection();
        area.getSelectionModel().clearSelection();
        event.getSelectionModel().clearSelection();
        otherArea.clear();
        otherEvents.clear();
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
                    javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Logout Failed", "Unable to load the login screen.");
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(type.name());
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void del3(ActionEvent event) {
        Person selected2 = table.getSelectionModel().getSelectedItem();
        if (selected2 != null) {
            personnelList.remove(selected2);
        }

    }

    public static class Person {

        private final SimpleStringProperty id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty birthDate;
        private final SimpleStringProperty purpose;
        private final SimpleStringProperty event;

        public Person(String firstName, String lastName, String gender, String birthDate, String purpose) {
            this(firstName, lastName, gender, birthDate, purpose, "");
        }

        public Person(String firstName, String lastName, String gender, String birthDate) {
            this(firstName, lastName, gender, birthDate, "", "");
        }

        public Person(String firstName, String lastName, String gender, String birthDate, String purpose, String event) {
            this.id = new SimpleStringProperty(String.valueOf((int) (Math.random() * 9000 + 1000)));
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.gender = new SimpleStringProperty(gender);
            this.birthDate = new SimpleStringProperty(birthDate);
            this.purpose = new SimpleStringProperty(purpose);
            this.event = new SimpleStringProperty(event);
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

        public String getEvent() {
            return event.get();
        }
    }
}
