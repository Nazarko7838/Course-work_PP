package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TruckTest {

    private Truck truck;
    private Coffee coffee1;
    private Coffee coffee2;

    @BeforeEach
    void setUp() {
        truck = new Truck(10.0); // місткість 10
        coffee1 = new Coffee("Зернова", "Банка", 1.0, 1.0, 100.0); // volume = 1.0
        coffee2 = new Coffee("Мелена", "Пачка", 2.0, 1.0, 150.0); // volume = 2.0
    }

    @Test
    void testGetCapacity() {
        assertEquals(10.0, truck.getCapacity(), 0.001, "Місткість фургона має бути 10.0");
    }

    @Test
    void testInitialLoadedVolumeIsZero() {
        assertEquals(0.0, truck.getLoadedVolume(), 0.001, "Спочатку обʼєм завантаження має бути 0.0");
    }

    @Test
    void testCanLoadTrue() {
        assertTrue(truck.canLoad(coffee1, 3), "Повинно бути можливо завантажити 3 банки по 1.0 = 3.0 <= 10.0");
    }

    @Test
    void testCanLoadFalse() {
        assertFalse(truck.canLoad(coffee2, 6), "Не повинно бути можливо завантажити 6 пачок по 2.0 = 12.0 > 10.0");
    }

    @Test
    void testLoadCoffee() {
        truck.loadCoffee(coffee1, 2);
        Map<Coffee, Integer> loaded = truck.getLoadedCoffeeDetails();

        assertTrue(loaded.containsKey(coffee1), "Завантажена кава повинна бути в мапі");
        assertEquals(2, loaded.get(coffee1), "Кількість кави має бути 2");
    }

    @Test
    void testGetLoadedVolumeAfterLoading() {
        truck.loadCoffee(coffee1, 3); // 3 * 1.0 = 3.0
        truck.loadCoffee(coffee2, 2); // 2 * 2.0 = 4.0
        assertEquals(7.0, truck.getLoadedVolume(), 0.001, "Загальний обʼєм повинен бути 7.0");
    }

    @Test
    void testLoadSameCoffeeTwice() {
        truck.loadCoffee(coffee1, 1);
        truck.loadCoffee(coffee1, 2);
        assertEquals(3, truck.getLoadedCoffeeDetails().get(coffee1), "Загальна кількість має бути 3");
    }

    @Test
    void testSetAndGetId() {
        truck.setId(42L);
        assertEquals(42L, truck.getId(), "ID повинен бути 42");
    }

    @Test
    void testToString() {
        truck.setId(7);
        String output = truck.toString();
        assertTrue(output.contains("Фургон #7"), "Повинно містити правильний ID");
        assertTrue(output.contains("Обʼєм: 10.0"), "Повинно містити обʼєм фургона");
    }
}
