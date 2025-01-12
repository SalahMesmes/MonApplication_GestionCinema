package dataBaseSQL;

import classes.Movie;
import classes.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomSQL {
    static Connection connection = ConnectionBDD.ConnectionBDD();

    public static SQLException AddRoom(Room room){
        String sql = "INSERT INTO Room (`Name`, `Capacity`, `Color`) VALUES (?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, room.getName());
            preparedStatement.setInt(2, room.getCapacity());
            preparedStatement.setString(3, room.getColor());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
            return ex;
        }
        return null;
    }

    public static List<Room> GetRooms(){
        String sql = "SELECT * FROM Room";
        List<Room> rooms = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql);) {
            while(resultSet.next()){
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("Name");
                int capacity = resultSet.getInt("Capacity");
                String color = resultSet.getString("Color");

                Room room = new Room(id, name, capacity, color);
                rooms.add(room);
            }
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }
        return rooms;
    }

    public static Room GetRoomByIdForDisplay(int roomId){
        String sql = "SELECT * FROM Room WHERE Id = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roomId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("Id");
                    String name = resultSet.getString("Name");
                    int capacity = resultSet.getInt("Capacity");
                    String color = resultSet.getString("Color");

                    return new Room(id, name, capacity, color);
                }
            }
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }
        return null;
    }

    public static SQLException UpdateRoom(Room room){
        String sql = "UPDATE Room SET Name = ?, Capacity = ?, Color = ? WHERE Id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, room.getName());
            preparedStatement.setInt(2, room.getCapacity());
            preparedStatement.setString(3, room.getColor());
            preparedStatement.setInt(4, room.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
            return ex;
        }
        return null;
    }

    public static SQLException DeleteRoom(Room room){
        String sql = "DELETE FROM Room WHERE Id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, room.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
            return ex;
        }
        return null;
    }
}
