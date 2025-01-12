package classes;

import java.sql.Date;

public class Movie {
    private int id;
    private String name;
    private String details;
    private String gender;
    private Date releaseDate;
    private int duration;
    private String color;

    public Movie(int id, String name, String details, String gender, Date releaseDate, int duration, String color) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.gender = gender;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getGender() {
        return gender;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public String getColor() {
        return color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
