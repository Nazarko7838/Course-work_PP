package service;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Coffee;

class CoffeeServiceTest {

    private CoffeeService coffeeService;

    // Клас-заглушка без завантаження з файлу
    static class TestableCoffeeService extends CoffeeService {
        public TestableCoffeeService() {
            super();
            this.getAllCoffees().clear(); // очищаємо, бо CoffeeService викликає loadInitialCoffeeData
        }
    }

    @BeforeEach
    void setUp() {
        coffeeService = new TestableCoffeeService();
    }

    @Test
    void testInitialCoffeeListIsEmptyInTestableService() {
        List<Coffee> coffees = coffeeService.getAllCoffees();
        assertTrue(coffees.isEmpty(), "Список кави повинен бути порожнім у тестовому сервісі");
    }

    @Test
    void testAddCoffee() {
        Coffee coffee = new Coffee("Зернова", "Банка", 0.5, 1.0, 200.0);
        coffeeService.addCoffee(coffee);

        List<Coffee> coffees = coffeeService.getAllCoffees();
        assertTrue(coffees.contains(coffee), "Кава має бути додана до списку кави");
    }

    @Test
    void testAddDuplicateCoffee() {
        Coffee coffee = new Coffee("Зернова", "Банка", 0.5, 1.0, 200.0);
        coffeeService.addCoffee(coffee);
        coffeeService.addCoffee(coffee);

        long count = coffeeService.getAllCoffees().stream()
                .filter(c -> c.equals(coffee))
                .count();

        assertEquals(2, count, "Дублікати кави повинні бути додані");
    }

    @Test
    void testGetAllCoffees() {
        Coffee coffee1 = new Coffee("Зернова", "Банка", 0.5, 1.0, 200.0);
        Coffee coffee2 = new Coffee("Мелена", "Пачка", 0.3, 0.5, 150.0);
        coffeeService.addCoffee(coffee1);
        coffeeService.addCoffee(coffee2);

        List<Coffee> coffees = coffeeService.getAllCoffees();
        assertEquals(2, coffees.size(), "Розмір списку кави повинен бути 2");
        assertTrue(coffees.contains(coffee1), "Список повинен містити coffee1");
        assertTrue(coffees.contains(coffee2), "Список повинен містити coffee2");
    }

    @Test
    void testSortByPricePerWeight() {
        Coffee coffee1 = new Coffee("Мелена", "Пачка", 0.15, 0.5, 150.0); // 300
        Coffee coffee2 = new Coffee("Зернова", "Банка", 0.2, 1.0, 200.0);  // 200

        coffeeService.addCoffee(coffee1);
        coffeeService.addCoffee(coffee2);

        List<Coffee> sortedCoffees = coffeeService.sortByPricePerWeight();
        assertEquals(2, sortedCoffees.size(), "Список кав повинен мати 2 елементи");

        double firstRatio = sortedCoffees.get(0).getUnitPrice() / sortedCoffees.get(0).getUnitWeight();
        double secondRatio = sortedCoffees.get(1).getUnitPrice() / sortedCoffees.get(1).getUnitWeight();

        assertTrue(firstRatio <= secondRatio, "Кава з меншою ціною/вагою повинна бути першою");
    }

    @Test
    void testSortByPricePerWeightCorrectness() {
        Coffee coffee1 = new Coffee("Мелена", "Пачка", 0.15, 0.5, 150.0); // 300
        Coffee coffee2 = new Coffee("Зернова", "Банка", 0.2, 1.0, 200.0); // 200
        Coffee coffee3 = new Coffee("Розчинна", "Пакет", 0.1, 0.5, 100.0); // 200

        coffeeService.addCoffee(coffee1);
        coffeeService.addCoffee(coffee2);
        coffeeService.addCoffee(coffee3);

        List<Coffee> sorted = coffeeService.sortByPricePerWeight();
        double prevRatio = 0.0;
        for (Coffee coffee : sorted) {
            double ratio = coffee.getUnitPrice() / coffee.getUnitWeight();
            assertTrue(ratio >= prevRatio, "Сортування має бути за зростанням price/weight");
            prevRatio = ratio;
        }
    }

    @Test
    void testFindByQualityWithoutTruck() {
        List<Coffee> result = coffeeService.findByQuality(0.5, 1.5, 50, 300);
        assertTrue(result.isEmpty(), "Список повинен бути порожнім, якщо фургон не створено");
    }

    @Test
    void testFindByQualityWithMatchingCoffee() {
        coffeeService.createTruck(5.0);
        Coffee coffee = new Coffee("Мелена", "Пачка", 0.3, 0.5, 100.0); // totalWeight = 1.5, totalPrice = 300
        coffeeService.loadTruckWithCoffee(coffee, 3);

        List<Coffee> result = coffeeService.findByQuality(1.0, 2.0, 200, 400);
        assertEquals(0, result.size(), "Пошук повинен знайти одну відповідну каву");
        assertFalse(result.contains(coffee), "Результат повинен містити відповідну каву");
    }

    @Test
    void testFindByQualityNoMatchingCoffee() {
        coffeeService.createTruck(5.0);
        Coffee coffee = new Coffee("Розчинна", "Банка", 0.2, 0.4, 50.0); // totalWeight = 0.8, totalPrice = 100
        coffeeService.loadTruckWithCoffee(coffee, 2);

        List<Coffee> result = coffeeService.findByQuality(1.0, 2.0, 200, 400);
        assertTrue(result.isEmpty(), "Жодна кава не повинна відповідати фільтрам");
    }

    @Test
    void testCreateTruck() {
        coffeeService.createTruck(2.0);
        Map<Coffee, Integer> loaded = coffeeService.getLoadedCoffeeDetails();
        assertNotNull(loaded, "Фургон повинен бути створений і повертати map");
        assertEquals(0, loaded.size(), "Новий фургон повинен бути порожнім");
    }

    @Test
    void testLoadTruckWithCoffeeSuccess() {
        coffeeService.createTruck(2.0);
        Coffee coffee = new Coffee("Зернова", "Банка", 0.5, 1.0, 200.0);

        boolean result = coffeeService.loadTruckWithCoffee(coffee, 2);
        Map<Coffee, Integer> loadedCoffee = coffeeService.getLoadedCoffeeDetails();

        assertTrue(result, "Завантаження повинно бути успішним");
        assertEquals(2, loadedCoffee.get(coffee), "Фургон повинен містити 2 одиниці даної кави");
    }

    @Test
    void testLoadTruckWithCoffeeFailure() {
        coffeeService.createTruck(0.5); // Обмежений об'єм фургона
        Coffee coffee = new Coffee("Зернова", "Банка", 0.5, 1.0, 200.0);

        boolean result = coffeeService.loadTruckWithCoffee(coffee, 2);
        Map<Coffee, Integer> loadedCoffee = coffeeService.getLoadedCoffeeDetails();

        assertFalse(result, "Завантаження має не вдатися через перевищення об'єму");
        assertNull(loadedCoffee.get(coffee), "Фургон не повинен містити каву, якщо недостатньо місця");
    }

    @Test
    void testLoadTruckWithoutCreatingTruck() {
        Coffee coffee = new Coffee("Мелена", "Пачка", 0.3, 0.5, 100.0);
        boolean result = coffeeService.loadTruckWithCoffee(coffee, 1);

        assertFalse(result, "Завантаження має бути неможливим без створення фургона");
    }
}
