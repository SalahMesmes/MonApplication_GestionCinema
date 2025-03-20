package test;

import classes.Movie;
import classes.Price;
import dataBaseSQL.MovieSQL;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class MovieSQLTest {
    @Test
    public void testCrudMovie() {
        List<Movie> movies = MovieSQL.GetMovies();
        assertNotNull(movies);
        int movieSize = movies.size();
        assertTrue(movieSize > 0);
        Movie lastMovieInBDD = movies.get(movieSize-1);

        int genderId = 1; // Remplacez ceci par l'ID du genre approprié dans votre base de données
        SQLException addException = MovieSQL.AddMovie(new Movie((lastMovieInBDD.getId()+1), "TestMovie", "TestDetails", "", Date.valueOf(LocalDate.now()), 120, "TestColor"), genderId);
        assertNull(addException);

        movies = MovieSQL.GetMovies();
        assertNotNull(movies);
        assertEquals(movies.size(), movieSize+1);

        Movie lastMovie = movies.get(movies.size() - 1);

        String newMovieName = "newMovieName";
        String newMovieDetails = "newMovieDetails";
        int newMovieDuration = 60;
        lastMovie.setName(newMovieName);
        lastMovie.setDetails(newMovieDetails);
        lastMovie.setDuration(newMovieDuration);
        SQLException updateException = MovieSQL.UpdateMovie(lastMovie, genderId);
        assertNull(updateException);

        Movie movie = MovieSQL.GetMovieByIdForDisplay(lastMovie.getId());
        assertNotNull(movie);
        assertEquals(movie.getName(), newMovieName);
        assertEquals(movie.getDetails(), newMovieDetails);
        assertEquals(movie.getDuration(), newMovieDuration);

        SQLException deleteException = MovieSQL.DeleteMovie(movie);
        assertNull(deleteException);

        movies = MovieSQL.GetMovies();
        assertNotNull(movies);
        assertEquals(movies.size(), movieSize);
    }
}