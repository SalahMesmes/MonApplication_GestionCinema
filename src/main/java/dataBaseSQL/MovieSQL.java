package dataBaseSQL;

import classes.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieSQL {
    static Connection connection = ConnectionBDD.ConnectionBDD();

    public static SQLException AddMovie(Movie movie, int genderId){
        String sql = "INSERT INTO Movie (`Name`, `Details`, `ReleaseDate`, `Duration`, `GenderId`, `Color`) " +
                "VALUES (?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getName());
            preparedStatement.setString(2, movie.getDetails());
            preparedStatement.setDate(3, movie.getReleaseDate());
            preparedStatement.setInt(4, movie.getDuration());
            preparedStatement.setInt(5, genderId);
            preparedStatement.setString(6, movie.getColor());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
            return ex;
        }
        return null;
    }

    public static List<Movie> GetMovies(){
        String sql = "SELECT * FROM Movie INNER JOIN Gender ON Movie.GenderId = Gender.Id";
        List<Movie> movies = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql);) {
            while(resultSet.next()){
                int id = resultSet.getInt("Movie.Id");
                String name = resultSet.getString("Movie.Name");
                String details = resultSet.getString("Movie.Details");
                String gender = resultSet.getString("Gender.Name");
                Date releaseDate = resultSet.getDate("Movie.ReleaseDate");
                int duration = resultSet.getInt("Movie.Duration");
                String color = resultSet.getString("Movie.Color");

                Movie movie = new Movie(id, name, details, gender, releaseDate, duration, color);
                movies.add(movie);
            }
        } catch (SQLException ex) {
            //Handle any errors
            System.out.println("SQLException : " +ex.getMessage());
            System.out.println("SQLState : " + ex.getSQLState());
            System.out.println("VendorError : " + ex.getErrorCode());
        }
        return movies;
    }

    public static Movie GetMovieByIdForDisplay(int movieId){
        String sql = "SELECT * FROM Movie INNER JOIN Gender ON Movie.GenderId = Gender.Id WHERE Movie.Id = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, movieId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("Movie.Id");
                    String name = resultSet.getString("Movie.Name");
                    String details = resultSet.getString("Movie.Details");
                    String gender = resultSet.getString("Gender.Name");
                    Date releaseDate = resultSet.getDate("Movie.ReleaseDate");
                    int duration = resultSet.getInt("Movie.Duration");
                    String color = resultSet.getString("Movie.Color");

                    return new Movie(id, name, details, gender, releaseDate, duration, color);
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

    public static SQLException UpdateMovie(Movie movie, int genderId){
        String sql = "UPDATE Movie SET Name = ?, Details = ?, GenderId = ?, ReleaseDate = ?, Duration = ? , Color = ?" +
                " WHERE Id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getName());
            preparedStatement.setString(2, movie.getDetails());
            preparedStatement.setInt(3, genderId);
            preparedStatement.setDate(4, movie.getReleaseDate());
            preparedStatement.setInt(5, movie.getDuration());
            preparedStatement.setString(6, movie.getColor());
            preparedStatement.setInt(7, movie.getId());

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

    public static SQLException DeleteMovie(Movie movie){
        String sql = "DELETE FROM Movie WHERE Id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, movie.getId());
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
