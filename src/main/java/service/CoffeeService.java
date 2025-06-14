package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import database.TruckCoffeeDAO;
import database.TruckDAO;
import model.Coffee;
import model.Truck;

public class CoffeeService {
    private List<Coffee> coffees;
    private Truck truck;
    private TruckDAO truckDAO = new TruckDAO();
    private TruckCoffeeDAO coffeeDAO = new TruckCoffeeDAO();
    private long currentTruckId = -1;

    // Структура для зберігання кави у фургоні
    private Map<Coffee, Integer> loadedCoffeeMap = new HashMap<>();

    public CoffeeService() {
        this.coffees = new ArrayList<>();
        loadInitialCoffeeData("src\\main\\resources\\initial_coffee_data.txt");
    }

    private void loadInitialCoffeeData(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length == 5) {
                    String coffeeType = data[0];
                    String physicalState = data[1];
                    double volume = Double.parseDouble(data[2]);
                    double weight = Double.parseDouble(data[3]);
                    double price = Double.parseDouble(data[4]);
                    Coffee coffee = new Coffee(coffeeType, physicalState, volume, weight, price);
                    coffees.add(coffee);
                }
            }
            System.out.println("Початковий набiр кави завантажено.");
        } catch (IOException e) {
            System.err.println("Помилка при завантаженнi початкових даних кави: " + e.getMessage());
        }
    }

    public void addCoffee(Coffee coffee) {
        coffees.add(coffee);
    }

    public List<Coffee> getAllCoffees() {
        return coffees;
    }

    public List<Coffee> sortByPricePerWeight() {
        return coffees.stream()
                .sorted(Comparator.comparingDouble(c -> c.getUnitPrice() / c.getUnitWeight()))
                .collect(Collectors.toList());
    }

    public List<Coffee> findByQuality(double minWeight, double maxWeight, double minPrice, double maxPrice) {
        if (truck == null) {
            System.out.println("Фургон ще не створено.");
            return new ArrayList<>();
        }
        return loadedCoffeeMap.entrySet().stream()
                .filter(entry -> entry.getKey().getTotalWeight() >= minWeight
                        && entry.getKey().getTotalWeight() <= maxWeight)
                .filter(entry -> entry.getKey().getTotalPrice() >= minPrice
                        && entry.getKey().getTotalPrice() <= maxPrice)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void createTruck(double capacity) {
    this.truck = new Truck(capacity);
    this.currentTruckId = truckDAO.saveTruck(truck);
    this.loadedCoffeeMap.clear(); 
    System.out.println("Фургон створено з ID = " + currentTruckId);
}


    public double getLoadedVolume() {
        return loadedCoffeeMap.entrySet().stream()
                .mapToDouble(e -> e.getKey().getTotalVolume() * e.getValue())
                .sum();
    }

    public boolean canLoad(Coffee coffee, int quantity) {
        return (getLoadedVolume() + coffee.getTotalVolume() * quantity) <= truck.getCapacity();
    }

    public boolean loadTruckWithCoffee(Coffee coffee, int quantity) {
        if (truck == null || currentTruckId == -1) {
            System.out.println("Фургон ще не створено.");
            return false;
        }

        if (canLoad(coffee, quantity)) {
            loadedCoffeeMap.put(coffee, loadedCoffeeMap.getOrDefault(coffee, 0) + quantity);
            System.out.println("Кава " + coffee.getCoffeeType() + " завантажена у фургон у кiлькостi " + quantity + ".");
            coffeeDAO.saveCoffee(currentTruckId, coffee, quantity);
            return true;
        } else {
            System.out.println("Недостатньо мiсця у фургонi.");
            return false;
        }
    }

    public Map<Coffee, Integer> getLoadedCoffeeDetails() {
        return loadedCoffeeMap;
    }
}
