package controllers;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.io.IOException;
import java.sql.SQLException;

public class CinemaManagementController {
    protected void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showDefaultErrorAlert(String detail, SQLException exception) {

        showAlert("Erreur", "Une erreur est survenue" + detail + (exception != null ? ".Message d'erreur : " + exception.getMessage(): ""),
                Alert.AlertType.ERROR);
    }

    public static double[] setScreenSize(double screenHeightPercentage, double screenWidthPercentage) {
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        double windowHeight = screenHeight * screenHeightPercentage;
        double windowWidth = screenWidth * screenWidthPercentage;
        double[] screenSize = new double[2];
        screenSize[0] = windowHeight;
        screenSize[1] = windowWidth;
        return screenSize;
    }

    public static String decodeColorInHex(Color color){
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
}
