/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ssoms;

import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.PasswordField;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private Button LoginButton;

    @FXML
    private void initialize() {

        LoginButton.setOnMouseEntered((MouseEvent e) -> {
            LoginButton.setStyle("-fx-background-color: #3CB371; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 25;");
        });

        LoginButton.setOnMouseExited((MouseEvent e) -> {
            LoginButton.setStyle("-fx-background-color: #2E8B57; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 25;");
        });

        LoginButton.setOnMousePressed((MouseEvent e) -> {
            LoginButton.setStyle("-fx-background-color: #276749; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 25;");
        });

        LoginButton.setOnMouseReleased((MouseEvent e) -> {
            LoginButton.setStyle("-fx-background-color: #3CB371; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 25;");
        });
    }

    @FXML
    public void lgnButtonOnAction(ActionEvent event) throws IOException {
        String usern = usernameLogin.getText();
        String passw = passwordLogin.getText();

        if (usern.equalsIgnoreCase("admin") && passw.equals("123")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomepageForm.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("SSOMS");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }

}
