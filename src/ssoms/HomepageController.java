
package ssoms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


public class HomepageController {

    @FXML
    private TableView<?> table;
    @FXML
    private TextField lastName;
    @FXML
    private Text upperlbl;
    @FXML
    private Text lowerlbl;
    @FXML
    private TableColumn<?, ?> idNUm;
    @FXML
    private TextField firstName;
    @FXML
    public Button perinf;
    @FXML
    public Button orgChart;
    @FXML
    public Button visLog;
    @FXML
    public Button incidentRep;
    @FXML
    private TableColumn<?, ?> firstNameColumn;
    @FXML
    private TableColumn<?, ?> lastNameColumn;

    @FXML
    private void addPersonel(ActionEvent event) {
    }
    
    @FXML
    private void handleClicks (ActionEvent event) {
        if (event.getSource() == perinf) {
            upperlbl.setText("PERSONNEL");
            lowerlbl.setText("home/personnels");
        }
        else if (event.getSource() == orgChart) {
            upperlbl.setText("ORGANIZATIONAL CHART");
            lowerlbl.setText("home/orgChart");
        }
         else if (event.getSource() == visLog) {
            upperlbl.setText("VISITOR's Logbook");
            lowerlbl.setText("home/visLog");
        }
         else if (event.getSource() == incidentRep) {
            upperlbl.setText("INCIDENT REPORTS");
            lowerlbl.setText("home/inciReport");
        }
    }

    
}
