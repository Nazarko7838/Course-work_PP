package database;

import model.Coffee;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TruckCoffeeDAOTest {

    private static TruckCoffeeDAO dao;

    @BeforeAll
    static void setup() {
        DatabaseManager.initializeDatabase();
        dao = new TruckCoffeeDAO();

        // Створимо фургон, щоб отримати його ID
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO trucks (name, capacity) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, "Test Truck");
            stmt.setDouble(2, 1000.0);
            stmt.executeUpdate();

            try (var rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    testTruckId = rs.getLong(1);
                }
            }

        } catch (Exception e) {
            fail("Не вдалося створити тестовий фургон: " + e.getMessage());
        }
    }

    private static long testTruckId;

    @Test
    void testSaveCoffeeAndGetCoffeeForTruck() {
        Coffee testCoffee = new Coffee("Зернова", "Банка", 0.2, 1.0, 200.0);
        int quantity = 10;

        dao.saveCoffee(testTruckId, testCoffee, quantity);
        Map<Coffee, Integer> coffeeMap = dao.getCoffeeForTruck(testTruckId);

        assertEquals(1, coffeeMap.size());
        assertFalse(coffeeMap.containsKey(testCoffee));
        assertEquals(null, coffeeMap.get(testCoffee));
    }

    @Test
    void testGetCoffeeForTruckWhenEmpty() {
        long fakeTruckId = 99999; // Несуществующий фургон
        Map<Coffee, Integer> coffeeMap = dao.getCoffeeForTruck(fakeTruckId);
        assertNotNull(coffeeMap);
        assertTrue(coffeeMap.isEmpty(), "Очікується порожній результат для фейкового ID");
    }

    @AfterAll
    static void cleanup() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM truck_coffee");
            stmt.execute("DELETE FROM trucks");
        } catch (Exception e) {
            System.err.println("Помилка при очищенні бази: " + e.getMessage());
        }
    }
}
