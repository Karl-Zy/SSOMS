package ssoms;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

/**
 *
 * @author Karlllllllllllllllll
 */
public class SSOMS extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Intro.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("SSOMS");
        stage.setScene(scene);

        stage.show();
        stage.setResizable(false);
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
