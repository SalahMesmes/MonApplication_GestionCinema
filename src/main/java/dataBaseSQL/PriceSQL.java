package dataBaseSQL;

import classes.Price;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PriceSQL {
    static Connection connection = ConnectionBDD.ConnectionBDD();

    public static SQLException AddPrice(Price price){
        String sql = "INSERT INTO `Price` (`Name`, `Cost`) VALUES (?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, price.getName());
            preparedStatement.setFloat(2, price.getCost());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
            return ex;
        }
        return null;
    }

    public static List<Price> GetPricing(){
        String sql = "SELECT * FROM Price";
        List<Price> pricing = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql);) {
            while(resultSet.next()){
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("Name");
                float cost = resultSet.getFloat("Cost");

                Price price = new Price(id, name, cost);
                pricing.add(price);
            }
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }
        return pricing;
    }

    public static SQLException UpdatePrice(Price price){
        String sql = "UPDATE Price SET Name = ?, Cost = ? WHERE Id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, price.getName());
            preparedStatement.setFloat(2, price.getCost());
            preparedStatement.setInt(3, price.getId());

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

    public static SQLException DeletePrice(Price price){
        String sql = "DELETE FROM Price WHERE Id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, price.getId());
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
