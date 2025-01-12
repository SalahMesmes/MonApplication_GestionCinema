package controllers;

import classes.Room;
import dataBaseSQL.RoomSQL;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RoomEditPopupController extends CinemaManagementController {
    @FXML
    public ColorPicker colorPicker;

    @FXML
    private TextField roomNameField;

    @FXML
    private TextField capacityField;

    private Stage stage;

    private Room roomToEdit;

    private MovieAndRoomListTabController movieAndRoomListTabController;

    public void setRoomEditPopup(Room room, Parent root, MovieAndRoomListTabController movieAndRoomListTabController) {
        this.movieAndRoomListTabController = movieAndRoomListTabController;
        roomToEdit = room;
        roomNameField.setText(room.getName());
        capacityField.setText(Integer.toString(room.getCapacity()));
        colorPicker.setValue(Color.web(room.getColor()));
        double[] screenSize = CinemaManagementController.setScreenSize(0.3, 0.2);
        stage = new Stage();
        stage.setScene(new Scene(root, screenSize[1], screenSize[0]));
        stage.setTitle("Modifier la salle");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void updateRoom() {
        String name = roomNameField.getText();
        if (name.isBlank()) {
            showAlert("Erreur", "Le nom est obligatoire", Alert.AlertType.ERROR);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityField.getText());
            if (capacity <= 0){
                showAlert("Erreur", "La capacité doit être positive",
                        Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e){
            showAlert("Erreur", "La capacité est obligatoire, il faut qu'elle soit au format numérique",
                    Alert.AlertType.ERROR);
            return;
        }

        stage.close();

        roomToEdit.setName(name);
        roomToEdit.setCapacity(capacity);
        roomToEdit.setColor(decodeColorInHex(colorPicker.getValue()));
        SQLException exception = RoomSQL.UpdateRoom(roomToEdit);
        if (exception != null){
            showDefaultErrorAlert(" lors de la modification de la salle", exception);
        } else {
            showAlert("Salle modifié", "La salle \"" + roomToEdit.getName() + "\" a été modifié avec succès !",
                    Alert.AlertType.INFORMATION);
            movieAndRoomListTabController.loadRoomData();
        }
    }
}
