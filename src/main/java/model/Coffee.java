package model;

public class Coffee {
    private String coffeeType;
    private String physicalState;
    private double volume;
    private double weight;
    private double price;

    public Coffee(String coffeeType, String physicalState, double volume, double weight, double price) {
        this.coffeeType = coffeeType;
        this.physicalState = physicalState;
        this.volume = volume;
        this.weight = weight;
        this.price = price;
    }


    public String getCoffeeType() { return coffeeType; }
    public String getPhysicalState() { return physicalState; }
    public double getUnitVolume() { return volume; }
    public double getUnitWeight() { return weight; }
    public double getUnitPrice() { return price; }

    public double getTotalVolume() { return volume; }
    public double getTotalWeight() { return weight; }
    public double getTotalPrice() { return price; }

    @Override
    public String toString() {
        return String.format("Кава [Тип: %s, Пакування: %s, Об'єм: %.2f л, Вага: %.2f кг, Цiна: %.2f грн]",
                coffeeType, physicalState, getTotalVolume(), getTotalWeight(), getTotalPrice());
    }
}
