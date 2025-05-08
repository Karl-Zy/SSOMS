package ssoms;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

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

    private ObservableList<Person> data = FXCollections.observableArrayList();
    @FXML
    private Button incidentRep1;
    @FXML
    private GridPane gp1;
    @FXML
    private GridPane gp2;
    @FXML
    private GridPane gp3;
    @FXML
    private GridPane gp4;

    public void initialize() {
        idNUm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        birthDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthDate()));
        table.setItems(data);
    }

    @FXML
    private void addPersonel(ActionEvent event) {
        String fn = firstName.getText().trim();
        String ln = lastName.getText().trim();
        String gen = gender.getText().trim();
        String bdate = (bday.getValue() != null) ? bday.getValue().toString() : "";

        if (!fn.isEmpty() && !ln.isEmpty() && !gen.isEmpty() && !bdate.isEmpty()) {
            data.add(new Person(fn, ln, gen, bdate));
            firstName.clear();
            lastName.clear();
            gender.clear();
            bday.setValue(null);
        }
    }

    @FXML
    private void handleClicks(ActionEvent event) {
        if (event.getSource() == perinf) {
            upperlbl.setText("PERSONNEL");
            lowerlbl.setText("home/personnels");
            gp1.toFront();
        } else if (event.getSource() == orgChart) {
            upperlbl.setText("ORGANIZATIONAL CHART");
            lowerlbl.setText("home/orgChart");
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

    public static class Person {

        private final SimpleStringProperty id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty birthDate;

        public Person(String firstName, String lastName, String gender, String birthDate) {
            this.id = new SimpleStringProperty(String.valueOf((int) (Math.random() * 9000 + 1000)));
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.gender = new SimpleStringProperty(gender);
            this.birthDate = new SimpleStringProperty(birthDate);
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
    }
}
