package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TruckTest {

    private Truck truck;

    @BeforeEach
    void setUp() {
        truck = new Truck(15.0); // новий об'єм
    }

    @Test
    void testGetCapacity() {
        assertEquals(15.0, truck.getCapacity(), 0.001, "Місткість фургона має бути 15.0");
    }

    @Test
    void testSetCapacity() {
        truck.setCapacity(20.0);
        assertEquals(20.0, truck.getCapacity(), 0.001, "Після зміни місткість має бути 20.0");
    }

    @Test
    void testSetAndGetId() {
        truck.setId(101);
        assertEquals(101, truck.getId(), "ID повинен бути 101");
    }

    @Test
    void testToStringContainsIdAndCapacity() {
        truck.setId(5);
        String str = truck.toString();
        assertTrue(str.contains("Фургон #5"), "toString повинен містити правильний ID");
        assertTrue(str.contains("Обʼєм: 15.0"), "toString повинен містити правильну місткість");
    }
}
