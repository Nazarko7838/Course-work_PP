package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import model.Coffee;

public class TruckCoffeeDAO {

    public void saveCoffee(long truckId, Coffee coffee, int quantity) {
        String sql = "INSERT INTO truck_coffee (truck_id, coffee_type, state, volume, weight, price, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, truckId);
            stmt.setString(2, coffee.getCoffeeType());
            stmt.setString(3, coffee.getPhysicalState());
            stmt.setDouble(4, coffee.getUnitVolume());
            stmt.setDouble(5, coffee.getUnitWeight());
            stmt.setDouble(6, coffee.getUnitPrice());
            stmt.setInt(7, quantity);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Coffee, Integer> getCoffeeForTruck(long truckId) {
        Map<Coffee, Integer> map = new HashMap<>();
        String sql = "SELECT * FROM truck_coffee WHERE truck_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, truckId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Coffee coffee = new Coffee(
                        rs.getString("coffee_type"),
                        rs.getString("state"),
                        rs.getDouble("volume"),
                        rs.getDouble("weight"),
                        rs.getDouble("price"));
                map.put(coffee, rs.getInt("quantity"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    
    
}
