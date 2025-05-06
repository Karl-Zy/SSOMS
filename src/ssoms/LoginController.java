/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ssoms;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Karlllllllllllllllll
 */
public class LoginController {

    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;

    @FXML
    private void lgnButtonOnAction(ActionEvent event) throws IOException {
         String usern = usernameLogin.getText();
        String passw = passwordLogin.getText();
     

        if (usern.equalsIgnoreCase("admin") && passw.equals("123")) {

            Parent root = FXMLLoader.load(getClass().getResource("HomepageForm.fxml"));

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setTitle("Verdant's Kitchen");
            primaryStage.show();
            primaryStage.setResizable(false);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }
        
    }
    

