package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Truck;

public class TruckDAO {

    public long saveTruck(Truck truck) {
        String sql = "INSERT INTO trucks (name, capacity) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, "Truck");
            stmt.setDouble(2, truck.getCapacity());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Truck> getAllTrucks() {
        List<Truck> trucks = new ArrayList<>();
        String sql = "SELECT id, capacity FROM trucks";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Truck truck = new Truck(rs.getDouble("capacity"));
                truck.setId(rs.getLong("id"));
                trucks.add(truck);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trucks;
    }

    public boolean deleteTruckById(long truckId) {
        String deleteCoffeeSql = "DELETE FROM truck_coffee WHERE truck_id = ?";
        String deleteTruckSql = "DELETE FROM trucks WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement deleteCoffeeStmt = conn.prepareStatement(deleteCoffeeSql);
                    PreparedStatement deleteTruckStmt = conn.prepareStatement(deleteTruckSql)) {

                deleteCoffeeStmt.setLong(1, truckId);
                deleteCoffeeStmt.executeUpdate();


                deleteTruckStmt.setLong(1, truckId);
                int affected = deleteTruckStmt.executeUpdate();

                conn.commit();
                return affected > 0;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
