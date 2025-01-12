package dataBaseSQL;

import classes.Movie;
import classes.Room;

import java.sql.*;
import java.util.HashMap;

public class SlotPricingSQL {
    static Connection connection = ConnectionBDD.ConnectionBDD();

    public static HashMap<Integer, Integer> GetSlotPricing(int slotId){
        String sql = "SELECT * FROM SlotPricing WHERE SlotPricing.SlotId = ?";
        HashMap<Integer, Integer> priceIdAndOccupiedSeat = new HashMap<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, slotId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int priceId = resultSet.getInt("PriceId");
                int occupiedSeat = resultSet.getInt("OccupiedSeat");

                priceIdAndOccupiedSeat.put(priceId, occupiedSeat);
            }
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }
        return priceIdAndOccupiedSeat;
    }

    public static SQLException AddSlotPricing(int slotId, int priceId, int occupiedSeat){
        String sql = "INSERT INTO `slotpricing` (`SlotId`, `PriceId`, `OccupiedSeat`) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, slotId);
            preparedStatement.setInt(2, priceId);
            preparedStatement.setInt(3, occupiedSeat);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
            return ex;
        }
        return null;
    }

    public static boolean GetSlotPricing(int slotId, int priceId){
        String sql = "SELECT * FROM `slotpricing` WHERE SlotId = ? AND PriceId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, slotId);
            preparedStatement.setInt(2, priceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }
        return false;
    }

    public static SQLException UpdateSlotPricing(int slotId, int priceId, int occupiedSeat){
        String sql = "UPDATE `slotpricing` SET `OccupiedSeat` = ? WHERE `SlotId` = ? AND `PriceId` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, occupiedSeat);
            preparedStatement.setInt(2, slotId);
            preparedStatement.setInt(3, priceId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
            return ex;
        }
        return null;
    }

    public static float GetIncome(Date startDate, Date endDate, Movie movie, Room room){
        String sql = "SELECT Slotpricing.OccupiedSeat, Price.Cost FROM Slotpricing " +
                "INNER JOIN Slot ON Slotpricing.SlotId = Slot.Id " +
                "INNER JOIN Price ON Slotpricing.PriceId = Price.Id " +
                "WHERE Slot.Date >= ? AND Slot.Date <= ?";
        if (movie != null){
            sql += " AND Slot.MovieId = ?";
            System.out.println(movie.getId());
        }
        if (room != null){
            sql += " AND Slot.RoomId = ?";
            System.out.println(room.getId());
        }
        System.out.println(sql);
        float todayIncome = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            if (movie != null){
                preparedStatement.setInt(3, movie.getId());
            }
            if (room != null){
                int index = 3;
                if (movie != null){
                    index = 4;
                }
                preparedStatement.setInt(index, room.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int occupiedSeat = resultSet.getInt(1);
                float cost = resultSet.getFloat(2);

                todayIncome += (occupiedSeat * cost);
            }
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }
        return todayIncome;
    }
}