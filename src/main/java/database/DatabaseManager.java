package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String JDBC_URL = "jdbc:h2:./coffee_db"; // Створюється файл coffee_db.mv.db
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String createTruckTable = """
                CREATE TABLE IF NOT EXISTS trucks (
                    id IDENTITY PRIMARY KEY,
                    name VARCHAR(255),
                    capacity DOUBLE
                );
            """;

            String createCoffeeTable = """
                CREATE TABLE IF NOT EXISTS truck_coffee (
                    id IDENTITY PRIMARY KEY,
                    truck_id BIGINT,
                    coffee_type VARCHAR(255),
                    state VARCHAR(50),
                    volume DOUBLE,
                    weight DOUBLE,
                    price DOUBLE,
                    quantity INT,
                    FOREIGN KEY (truck_id) REFERENCES trucks(id)
                );
            """;

            stmt.execute(createTruckTable);
            stmt.execute(createCoffeeTable);

            System.out.println("Базу даних ініціалізовано.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
