package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoffeeTest {

    private Coffee coffee;

    @BeforeEach
    void setUp() {
        // Ініціалізація об'єкта Coffee перед кожним тестом
        coffee = new Coffee("Зернова", "Банка", 0.5, 1.0, 200.0);
    }

    @Test
    void testGetCoffeeType() {
        assertEquals("Зернова", coffee.getCoffeeType(), "Тип кави не співпадає");
    }

    @Test
    void testGetPhysicalState() {
        assertEquals("Банка", coffee.getPhysicalState(), "Тип пакування не співпадає");
    }

    @Test
    void testGetUnitVolume() {
        assertEquals(0.5, coffee.getUnitVolume(), 0.001, "Об'єм не співпадає");
    }

    @Test
    void testGetUnitWeight() {
        assertEquals(1.0, coffee.getUnitWeight(), 0.001, "Вага не співпадає");
    }

    @Test
    void testGetUnitPrice() {
        assertEquals(200.0, coffee.getUnitPrice(), 0.001, "Ціна не співпадає");
    }

    @Test
    void testGetTotalVolume() {
        assertEquals(0.5, coffee.getTotalVolume(), 0.001, "Загальний об'єм не співпадає");
    }

    @Test
    void testGetTotalWeight() {
        assertEquals(1.0, coffee.getTotalWeight(), 0.001, "Загальна вага не співпадає");
    }

    @Test
    void testGetTotalPrice() {
        assertEquals(200.0, coffee.getTotalPrice(), 0.001, "Загальна ціна не співпадає");
    }

    @Test
    void testToString() {
        String expectedString = "Кава [Тип: Зернова, Пакування: Банка, Об'єм: 0,50 л, Вага: 1,00 кг, Цiна: 200,00 грн]";
        assertEquals(expectedString, coffee.toString(), "Результат toString() не співпадає з очікуваним");
    }
}
