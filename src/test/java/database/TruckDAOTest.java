package database;

import model.Truck;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TruckDAOTest {

    private static TruckDAO truckDAO;

    @BeforeAll
    static void setup() {
        DatabaseManager.initializeDatabase(); // Ініціалізація H2 або SQLite
        truckDAO = new TruckDAO();
    }

    @BeforeEach
    void cleanBefore() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM truck_coffee");
            stmt.execute("DELETE FROM trucks");
        } catch (Exception e) {
            fail("Помилка при очищенні перед тестом: " + e.getMessage());
        }
    }

    @Test
    void testSaveTruck() {
        Truck truck = new Truck(500.0);
        long id = truckDAO.saveTruck(truck);

        assertTrue(id > 0, "ID має бути більше нуля");
    }

    @Test
    void testGetAllTrucks() {
        Truck truck1 = new Truck(500.0);
        Truck truck2 = new Truck(1000.0);

        truckDAO.saveTruck(truck1);
        truckDAO.saveTruck(truck2);

        List<Truck> allTrucks = truckDAO.getAllTrucks();

        assertEquals(2, allTrucks.size());
        assertTrue(allTrucks.stream().anyMatch(t -> t.getCapacity() == 500.0));
        assertTrue(allTrucks.stream().anyMatch(t -> t.getCapacity() == 1000.0));
    }

    @Test
    void testDeleteTruckById() {
        Truck truck = new Truck(750.0);
        long id = truckDAO.saveTruck(truck);

        boolean deleted = truckDAO.deleteTruckById(id);

        assertTrue(deleted, "Фургон має бути успішно видалений");

        List<Truck> allTrucks = truckDAO.getAllTrucks();
        assertTrue(allTrucks.isEmpty(), "Список фургонів повинен бути порожнім");
    }

    @Test
    void testDeleteNonExistentTruck() {
        boolean result = truckDAO.deleteTruckById(9999L);
        assertFalse(result, "Неіснуючий фургон не повинен бути видалений");
    }
}
