package controllers;

import classes.Movie;
import classes.Room;
import dataBaseSQL.MovieSQL;
import dataBaseSQL.SlotSQL;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleAddSlotPopupController extends CinemaManagementController {
    private ScheduleTabController scheduleTabController;

    @FXML
    private Label labelForRoomAndTime;

    @FXML
    private Label labelForMovieDuration;

    @FXML
    private ComboBox<String> movieComboBox;

    private Stage stage;

    private List<Movie> movies;

    private Room roomForAdd;

    private int startHour;

    public void setScheduleAddSlotPopup(Room room, int time, Parent root, ScheduleTabController scheduleTabController) {
        this.scheduleTabController = scheduleTabController;
        roomForAdd = room;
        startHour = time;
        movies = MovieSQL.GetMovies();
        if (movies.isEmpty()){
            showAlert("Erreur", "Aucun film, rajouté un film avant", Alert.AlertType.ERROR);
            return;
        }
        List<String> moviesNames = new ArrayList<>();
        for (Movie movie: movies) {
            moviesNames.add(movie.getName());
        }
        movieComboBox.setItems(FXCollections.observableArrayList(moviesNames));
        movieComboBox.getSelectionModel().selectFirst();
        labelForRoomAndTime.setText("Pour la salle '" + room.getName() +"', à partir de " + (time > 9 ? time : "0" + time) + "h");

        movieComboBox.setOnAction(event -> {
            String selectedValue = movieComboBox.getValue();
            for (Movie movie: movies){
                if (movie.getName().equals(selectedValue)){
                    setLabelForMovieDuration(movie);
                }
            }
        });
        setLabelForMovieDuration(movies.get(0));
        labelForMovieDuration.setWrapText(true);
        double[] screenSize = CinemaManagementController.setScreenSize(0.3, 0.3);
        stage = new Stage();
        stage.setScene(new Scene(root, screenSize[1], screenSize[0]));
        stage.setTitle("Ajouter le film planning");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void setLabelForMovieDuration(Movie movie){
        int movieDuration = movie.getDuration();
        labelForMovieDuration.setText("Durée du film : " + movieDuration + " minutes, donc " + getSlotUsed(movieDuration) + " créneau(x) seront bloqué");
    }

    public static int getSlotUsed(int movieDuration){
        int slotUsed = movieDuration / 60;
        if (movieDuration % 60 != 0) {
            slotUsed++;
        }
        return slotUsed;
    }

    @FXML
    private void saveSlotSchedule() {
        int movieId = -1;
        for (Movie movie: movies){
            if (movie.getName().equals(movieComboBox.getValue())){
                movieId = movie.getId();
            }
        }
        stage.close();
        if (movieId == -1){
            showDefaultErrorAlert("", null);
            return;
        }

        SQLException exception = SlotSQL.AddSlot(roomForAdd.getId(), movieId, startHour);
        if (exception != null){
            showDefaultErrorAlert(" lors de l'ajout du film au planning", exception);
        } else {
            showAlert("Film ajouté au planning", "Le film \"" + movieComboBox.getValue() + "\" a été ajouté avec succès au planning !",
                    Alert.AlertType.INFORMATION);
            scheduleTabController.scheduleGridPane.setGridLinesVisible(false);
            scheduleTabController.scheduleGridPane.getChildren().clear();
            scheduleTabController.scheduleGridPane.setGridLinesVisible(true);
            scheduleTabController.refreshSchedule();
        }
    }
}
