package org.example.monapplication;

import controllers.CinemaManagementController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CinemaManagementApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("CinemaManagement.fxml"));
        primaryStage.setTitle("Gestion de cinéma");
        double[] screenSize = CinemaManagementController.setScreenSize(0.6, 0.6);
        primaryStage.setScene(new Scene(root, screenSize[1], screenSize[0]));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}