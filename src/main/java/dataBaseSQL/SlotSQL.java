package dataBaseSQL;

import classes.Movie;
import classes.Room;
import classes.Slot;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SlotSQL {
    static Connection connection = ConnectionBDD.ConnectionBDD();

    public static SQLException AddSlot(int roomId, int movieId, int startHour){
        String sql = "INSERT INTO Slot (`RoomId`, `MovieId`, `StartHour`, `Date`) VALUES (?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roomId);
            preparedStatement.setInt(2, movieId);
            preparedStatement.setInt(3, startHour);
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
            return ex;
        }
        return null;
    }

    public static List<Slot> GetTodaySlots(){
        String sql = "SELECT * FROM Slot WHERE Date = ?";
        List<Slot> slots = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("Id");
                int movieId = resultSet.getInt("MovieId");
                int roomId = resultSet.getInt("RoomId");
                int starHourRowLine = resultSet.getInt("StartHour");

                Movie movie = MovieSQL.GetMovieByIdForDisplay(movieId);
                Room room = RoomSQL.GetRoomByIdForDisplay(roomId);

                Slot slot = new Slot(id, room, movie, starHourRowLine);
                slots.add(slot);
            }
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }
        return slots;
    }
}
