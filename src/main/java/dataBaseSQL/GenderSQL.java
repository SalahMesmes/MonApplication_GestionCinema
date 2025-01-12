package dataBaseSQL;

import classes.Gender;
import classes.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenderSQL {
    static Connection connection = ConnectionBDD.ConnectionBDD();

    public static SQLException AddGender(String name){
        String sql = "INSERT INTO Gender (`Name`) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
            return ex;
        }
        return null;
    }

    public static List<Gender> GetGenders(){
        String sql = "SELECT * FROM Gender";
        List<Gender> genders = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql);) {
            while(resultSet.next()){
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("Name");
                Gender gender = new Gender(id, name);
                genders.add(gender);
            }
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }
        return genders;
    }
}
