package database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @BeforeAll
    static void setup() {
        // Ініціалізуємо базу перед усіма тестами
        DatabaseManager.initializeDatabase();
    }

    @Test
    void testGetConnection() {
        try (Connection conn = DatabaseManager.getConnection()) {
            assertNotNull(conn, "З'єднання не повинно бути null");
            assertFalse(conn.isClosed(), "З'єднання повинно бути відкритим");
        } catch (SQLException e) {
            fail("Помилка при отриманні з'єднання: " + e.getMessage());
        }
    }

    @Test
    void testInitializeDatabaseCreatesTables() {
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();

            try (ResultSet trucks = meta.getTables(null, null, "TRUCKS", null)) {
                assertTrue(trucks.next(), "Таблиця 'TRUCKS' повинна існувати");
            }

            try (ResultSet coffee = meta.getTables(null, null, "TRUCK_COFFEE", null)) {
                assertTrue(coffee.next(), "Таблиця 'TRUCK_COFFEE' повинна існувати");
            }

        } catch (SQLException e) {
            fail("Помилка при перевірці таблиць: " + e.getMessage());
        }
    }

    @Test
    void testInitializeDatabasePrintsOutput() {
        // Перехоплюємо стандартний потік виводу
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Викликаємо метод
        DatabaseManager.initializeDatabase();

        // Перевіряємо, що було виведено
        String output = outContent.toString().trim();
        assertTrue(output.contains("Базу даних ініціалізовано."));

        // Повертаємо стандартний потік
        System.setOut(originalOut);
    }


    @AfterAll
    static void cleanup() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS truck_coffee");
            stmt.execute("DROP TABLE IF EXISTS trucks");
        } catch (SQLException e) {
            System.err.println("Не вдалося очистити базу: " + e.getMessage());
        }
    }
}
