package classes;

public class Room {
    private int id;
    private String name;
    private int capacity;
    private String color;

    public Room(int id, String name, int capacity, String color) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
