package classes;

public class Slot {
    private int id;
    private Room room;
    private Movie movie;
    private int startHour;

    public Slot(int id, Room room, Movie movie, int startHour) {
        this.id = id;
        this.room = room;
        this.movie = movie;
        this.startHour = startHour;
    }

    public int getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

}
