package model;

public class Truck {
    private long id;
    private double capacity;

    public Truck(double capacity) {
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Фургон #" + id + ", Обʼєм: " + capacity;
    }
}
