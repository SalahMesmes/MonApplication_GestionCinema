package controllers;

import classes.Gender;
import classes.Movie;
import classes.Room;
import dataBaseSQL.GenderSQL;
import dataBaseSQL.MovieSQL;
import dataBaseSQL.RoomSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

public class MovieAndRoomListTabController extends CinemaManagementController {
    @FXML
    private TableView<Movie> movieTableView;

    @FXML
    private TableView<Room> roomTableView;

    @FXML
    private TextField movieNameFilterField;

    @FXML
    private ComboBox<String> movieGenderFilterComboBox;

    @FXML
    private DatePicker movieStartDateFilterDatePicker;

    @FXML
    private DatePicker movieEndDateFilterDatePicker;

    @FXML
    private TextField movieDurationFilterField;

    @FXML
    private Button resetMovieFiltersButton;

    @FXML
    private TextField roomNameFilterField;

    @FXML
    private Button resetRoomFiltersButton;

    private List<Movie> originalMovieList = new ArrayList<>();

    private List<Room> originalRoomList = new ArrayList<>();

    static ObservableMap<String, Integer> genderIds = FXCollections.observableHashMap();

    static List<String>  genderNames = new ArrayList<>();

    @FXML
    private void initialize() {
        configureColumns(this);
        loadGenderDataAndInitializeFilter();
        loadMovieData();
        loadRoomData();
    }

    public void configureColumns(MovieAndRoomListTabController movieAndRoomListTabController) {
        TableColumn<Movie, Void> actionColumn = (TableColumn<Movie, Void>) movieTableView.getColumns().get(7);

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            {
                editButton.setOnAction(event -> {
                    Movie movie = getTableView().getItems().get(getIndex());
                    movieEditPopup(movie, movieAndRoomListTabController);
                });

                deleteButton.setOnAction(event -> {
                    Movie movie = getTableView().getItems().get(getIndex());
                    SQLException exception = MovieSQL.DeleteMovie(movie);
                    originalMovieList.remove(movie);
                    movieTableView.setItems(FXCollections.observableArrayList(originalMovieList));
                    if (exception != null){
                        showDefaultErrorAlert(" lors de la suppression du film", exception);
                    } else {
                        showAlert("Film supprimé", "Le film \"" + movie.getName() + "\" a été supprimé avec succès !",
                                Alert.AlertType.INFORMATION);
                    }
                });
            }

            private void movieEditPopup(Movie movie, MovieAndRoomListTabController movieAndRoomListTabController) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cinemamanagement/MovieEditPopup.fxml"));
                    Parent root = loader.load();
                    MovieEditPopupController controller = loader.getController();
                    controller.setMovieEditPopup(movie, root, movieAndRoomListTabController);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    VBox buttonBox = new VBox(editButton, deleteButton);
                    buttonBox.setSpacing(5);
                    setGraphic(buttonBox);
                }
            }
        });

        TableColumn<Room, Void> actionColumnRoom = (TableColumn<Room, Void>) roomTableView.getColumns().get(4);
        actionColumnRoom.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setOnAction(event -> {
                    Room room = getTableView().getItems().get(getIndex());
                    roomEditPopup(room, movieAndRoomListTabController);
                });

                deleteButton.setOnAction(event -> {
                    Room room = getTableView().getItems().get(getIndex());
                    SQLException exception = RoomSQL.DeleteRoom(room);
                    originalRoomList.remove(room);
                    roomTableView.setItems(FXCollections.observableArrayList(originalRoomList));
                    if (exception != null){
                        showDefaultErrorAlert(" lors de la suppression de la salle", exception);
                    } else {
                        showAlert("Salle supprimé", "La salle \"" + room.getName() + "\" a été supprimé avec succès !",
                                Alert.AlertType.INFORMATION);
                    }
                });
            }

            private void roomEditPopup(Room room, MovieAndRoomListTabController movieAndRoomListTabController) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cinemamanagement/RoomEditPopup.fxml"));
                    Parent root = loader.load();
                    RoomEditPopupController controller = loader.getController();
                    controller.setRoomEditPopup(room, root, movieAndRoomListTabController);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    VBox buttonBox = new VBox(editButton, deleteButton);
                    buttonBox.setSpacing(5);
                    setGraphic(buttonBox);
                }
            }
        });
    }

    public void loadGenderDataAndInitializeFilter() {
        movieGenderFilterComboBox.setOnMouseClicked(mouseEvent -> {
            genderNames = new ArrayList<>();
            List<Gender> genders = GenderSQL.GetGenders();
            for (Gender gender: genders) {
                genderNames.add(gender.getName());
                genderIds.put(gender.getName(), gender.getId());
            }
            movieGenderFilterComboBox.setItems(FXCollections.observableArrayList(genderNames));
        });
    }

    public void loadMovieData() {
        List<Movie> movies = MovieSQL.GetMovies();
        if (!originalMovieList.isEmpty()){
            originalMovieList.clear();
        }
        if (!movies.isEmpty()){
            originalMovieList.addAll(movies);
        }
        movieTableView.setItems(FXCollections.observableArrayList(originalMovieList));
    }

    public void loadRoomData() {
        List<Room> rooms = RoomSQL.GetRooms();
        if (!originalRoomList.isEmpty()){
            originalRoomList.clear();
        }
        if (!rooms.isEmpty()){
            originalRoomList.addAll(rooms);
        }
        roomTableView.setItems(FXCollections.observableArrayList(originalRoomList));
    }

    @FXML
    private void applyMovieFilters() {
        // Get data from filter
        String nameFilter = movieNameFilterField.getText().toLowerCase();
        String genderFilter = movieGenderFilterComboBox.getValue();
        LocalDate startDateFilter = movieStartDateFilterDatePicker.getValue();
        LocalDate endDateFilter = movieEndDateFilterDatePicker.getValue();
        Integer durationFilter = ParseDuration(movieDurationFilterField.getText());

        // Filter table data from filtered data
        FilteredList<Movie> filteredData = movieTableView.getItems().filtered(movie -> {
            // Filter by name
            if (!nameFilter.isEmpty() && !movie.getName().toLowerCase().contains(nameFilter)) {
                return false;
            }
            // Filter by gender
            if (genderFilter != null && !genderFilter.isEmpty() && !movie.getGender().equalsIgnoreCase(genderFilter)) {
                return false;
            }
            // Filter by date
            if (startDateFilter != null && movie.getReleaseDate().toLocalDate().isBefore(startDateFilter)) {
                return false;
            }
            if (endDateFilter != null && movie.getReleaseDate().toLocalDate().isAfter(endDateFilter)) {
                return false;
            }
            // Filter by duration
            return durationFilter == null || movie.getDuration() == durationFilter;
        });

        // Update table data with filtered data
        movieTableView.setItems(filteredData);
        if (filteredData.size() != originalMovieList.size()){
            resetMovieFiltersButton.setDisable(false);
        }
    }

    private Integer ParseDuration(String durationString) {
        try {
            return Integer.parseInt(durationString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @FXML
    private void resetMovieFilters() {
        movieNameFilterField.clear();
        movieGenderFilterComboBox.getSelectionModel().clearSelection();
        movieStartDateFilterDatePicker.setValue(null);
        movieEndDateFilterDatePicker.setValue(null);
        movieDurationFilterField.clear();
        configureColumns(this);
        movieTableView.setItems(FXCollections.observableArrayList(originalMovieList));
        resetMovieFiltersButton.setDisable(true);
    }

    @FXML
    private void applyRoomFilters() {
        // Get data from filter
        String nameFilter = roomNameFilterField.getText().toLowerCase();
        FilteredList<Room> filteredData = roomTableView.getItems().filtered(room -> {
            // Filter by name
            return nameFilter.isEmpty() || room.getName().toLowerCase().contains(nameFilter);
        });

        // Update table data with filtered data
        roomTableView.setItems(filteredData);
        if (filteredData.size() != originalRoomList.size()){
            resetRoomFiltersButton.setDisable(false);
        }
    }

    @FXML
    private void resetRoomFilters() {
        roomNameFilterField.clear();
        configureColumns(this);
        roomTableView.setItems(FXCollections.observableArrayList(originalRoomList));
        resetRoomFiltersButton.setDisable(true);
    }

    @FXML
    public void refreshTables(ActionEvent actionEvent) {
        loadMovieData();
        loadRoomData();
    }
}
