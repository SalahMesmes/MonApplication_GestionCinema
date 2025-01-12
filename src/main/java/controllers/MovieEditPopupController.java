package controllers;

import classes.Gender;
import classes.Movie;
import dataBaseSQL.GenderSQL;
import dataBaseSQL.MovieSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieEditPopupController extends CinemaManagementController {
    @FXML
    public ColorPicker colorPicker;

    @FXML
    private TextField movieNameField;

    @FXML
    private TextArea movieDetailsArea;

    @FXML
    private ComboBox<String> movieGenderComboBox;

    @FXML
    private DatePicker movieYearDatePicker;

    @FXML
    private TextField movieDurationField;

    private Stage stage;

    private Movie movieToEdit;

    static List<String>  genderNames = new ArrayList<>();

    static ObservableMap<String, Integer> genderIds = FXCollections.observableHashMap();

    private MovieAndRoomListTabController movieAndRoomListTabController;

    public void setMovieEditPopup(Movie movie, Parent root, MovieAndRoomListTabController movieAndRoomListTabController) {
        this.movieAndRoomListTabController = movieAndRoomListTabController;
        movieToEdit = movie;
        movieNameField.setText(movie.getName());
        movieDetailsArea.setText(movie.getDetails());

        genderNames = new ArrayList<>();
        List<Gender> genders = GenderSQL.GetGenders();
        for (Gender gender: genders) {
            genderNames.add(gender.getName());
            genderIds.put(gender.getName(), gender.getId());
        }
        movieGenderComboBox.setItems(FXCollections.observableArrayList(genderNames));
        movieGenderComboBox.setValue(movie.getGender());

        movieYearDatePicker.setValue(movie.getReleaseDate().toLocalDate());
        movieDurationField.setText(Integer.toString(movie.getDuration()));
        colorPicker.setValue(Color.web(movie.getColor()));
        double[] screenSize = CinemaManagementController.setScreenSize(0.7, 0.3);
        stage = new Stage();
        stage.setScene(new Scene(root, screenSize[1], screenSize[0]));
        stage.setTitle("Modifier le film");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void updateMovie() {
        String name = movieNameField.getText();
        if (name.isBlank()){
            showAlert("Erreur", "Le nom est obligatoire", Alert.AlertType.ERROR);
            return;
        }

        LocalDate date = movieYearDatePicker.getValue();
        if (date == null){
            showAlert("Erreur", "La date est obligatoire", Alert.AlertType.ERROR);
            return;
        }

        List<Gender> genders = GenderSQL.GetGenders();
        for (Gender gender: genders) {
            genderIds.put(gender.getName(), gender.getId());
        }
        int genderId;
        try {
            genderId = genderIds.get(movieGenderComboBox.getValue());
        } catch (NullPointerException e){
            showAlert("Erreur", "Le genre est obligatoire", Alert.AlertType.ERROR);
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(movieDurationField.getText());
            if (duration <= 0){
                showAlert("Erreur", "La durée doit être positive",
                        Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e){
            showAlert("Erreur", "La durée est obligatoire, il faut qu'elle soit au format numérique",
                    Alert.AlertType.ERROR);
            return;
        }

        stage.close();

        movieToEdit.setName(name);
        movieToEdit.setGender(movieGenderComboBox.getValue());
        movieToEdit.setReleaseDate(Date.valueOf(date));
        movieToEdit.setDuration(duration);
        movieToEdit.setDetails(movieDetailsArea.getText());
        movieToEdit.setColor(decodeColorInHex(colorPicker.getValue()));
        SQLException exception = MovieSQL.UpdateMovie(movieToEdit, genderId);
        if (exception != null){
            showDefaultErrorAlert(" lors de la modification du film", exception);
        } else {
            showAlert("Film modifié", "Le film \"" + movieToEdit.getName() + "\" a été modifié avec succès !",
                    Alert.AlertType.INFORMATION);
            movieAndRoomListTabController.loadMovieData();
        }
    }
}
