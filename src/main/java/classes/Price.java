package classes;

public class Price {
    private int id;
    private String Name;
    private float Cost;

    public Price(int id, String name, float cost) {
        this.id = id;
        this.Name = name;
        this.Cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public float getCost() {
        return Cost;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setCost(float cost) {
        this.Cost = cost;
    }

    @Override
    public String toString() {
        return this.Name;
    }
}
