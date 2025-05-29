package ssoms;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IntroController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void logbook(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("Log-Report.fxml")); // make sure the FXML name is correct
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void login(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("LoginForm.fxml")); // change to your login FXML file
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
