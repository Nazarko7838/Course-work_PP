package model;

import java.util.HashMap;
import java.util.Map;

public class Truck {
    private double capacity;
    private Map<Coffee, Integer> loadedCoffee; // Зберігає каву та кількість

    public Truck(double capacity) {
        this.capacity = capacity;
        this.loadedCoffee = new HashMap<>();
    }

    public double getCapacity() {
        return capacity;
    }

    public double getLoadedVolume() {
        return loadedCoffee.entrySet().stream()
                .mapToDouble(e -> e.getKey().getTotalVolume() * e.getValue())
                .sum();
    }

    public boolean canLoad(Coffee coffee, int quantity) {
        return (getLoadedVolume() + coffee.getTotalVolume() * quantity) <= capacity;
    }

    public void loadCoffee(Coffee coffee, int quantity) {
        loadedCoffee.put(coffee, loadedCoffee.getOrDefault(coffee, 0) + quantity);
        System.out.println("Кава " + coffee.getCoffeeType() + " завантажена у фургон у кiлькостi " + quantity + ".");
    }

    public Map<Coffee, Integer> getLoadedCoffeeDetails() {
        return loadedCoffee; // Повертаємо мапу завантажених товарів
    }

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Фургон #" + id + ", Обʼєм: " + capacity;
    }

}
