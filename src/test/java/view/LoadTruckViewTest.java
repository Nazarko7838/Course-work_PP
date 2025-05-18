package view;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Coffee;
import service.CoffeeService;

public class LoadTruckViewTest extends ApplicationTest {

    private CoffeeService coffeeService;
    private LoadTruckView loadTruckView;

    @Override
    public void start(Stage stage) {
        coffeeService = mock(CoffeeService.class);
        loadTruckView = new LoadTruckView(coffeeService);
        stage.setScene(new javafx.scene.Scene(loadTruckView, 600, 400));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // Очистити введення перед кожним тестом
        interact(() -> {
            for (javafx.scene.Node node : loadTruckView.getChildren()) {
                if (node instanceof TextField) {
                    ((TextField) node).clear();
                }
                if (node instanceof ListView) {
                    ((ListView<?>) node).getItems().clear();
                }
            }
        });
    }

    @Test
    public void testCreateTruckValidInput() {
        List<Coffee> coffees = Arrays.asList(
                new Coffee("Arabica", "в зернах", 100, 500, 0.5),
                new Coffee("Robusta", "мелена", 200, 700, 0.7)
        );
        when(coffeeService.getAllCoffees()).thenReturn(coffees);

        interact(() -> {
            ((TextField) loadTruckView.getChildren().get(0)).setText("150.5");
            ((Button) loadTruckView.getChildren().get(1)).fire();
        });

        verify(coffeeService).createTruck(150.5);
        verify(coffeeService).getAllCoffees();

        ListView<?> listView = (ListView<?>) loadTruckView.getChildren().get(2);
        assertEquals(2, listView.getItems().size());

        Label msgLabel = (Label) loadTruckView.getChildren().get(5);
        assertTrue(msgLabel.getText().contains("Фургон успішно створено"));
    }

    @Test
    public void testCreateTruckInvalidNumberFormat() {
        interact(() -> {
            ((TextField) loadTruckView.getChildren().get(0)).setText("abc");
            ((Button) loadTruckView.getChildren().get(1)).fire();
        });

        verify(coffeeService, never()).createTruck(anyDouble());

        Label msgLabel = (Label) loadTruckView.getChildren().get(5);
        assertTrue(msgLabel.getText().contains("Неправильний формат"));
    }

    @Test
    public void testLoadSelectedCoffeeSuccess() {
        Coffee coffee = new Coffee("Arabica", "в зернах", 100, 500, 0.5);
        when(coffeeService.getAllCoffees()).thenReturn(List.of(coffee));
        when(coffeeService.loadTruckWithCoffee(coffee, 2)).thenReturn(true);

        interact(() -> {
            ((TextField) loadTruckView.getChildren().get(0)).setText("100");
            ((Button) loadTruckView.getChildren().get(1)).fire();

            ListView<String> listView = (ListView<String>) loadTruckView.getChildren().get(2);
            listView.getSelectionModel().select(0);

            ((TextField) loadTruckView.getChildren().get(3)).setText("2");
            ((Button) loadTruckView.getChildren().get(4)).fire();
        });

        Label msgLabel = (Label) loadTruckView.getChildren().get(5);
        assertTrue(msgLabel.getText().contains("Успішно завантажено"));
    }

    @Test
    public void testLoadSelectedCoffeeInsufficientCapacity() {
        Coffee coffee = new Coffee("Arabica", "в зернах", 100, 500, 0.5);
        when(coffeeService.getAllCoffees()).thenReturn(List.of(coffee));
        when(coffeeService.loadTruckWithCoffee(coffee, 2)).thenReturn(false);

        interact(() -> {
            ((TextField) loadTruckView.getChildren().get(0)).setText("100");
            ((Button) loadTruckView.getChildren().get(1)).fire();

            ListView<String> listView = (ListView<String>) loadTruckView.getChildren().get(2);
            listView.getSelectionModel().select(0);

            ((TextField) loadTruckView.getChildren().get(3)).setText("2");
            ((Button) loadTruckView.getChildren().get(4)).fire();
        });

        Label msgLabel = (Label) loadTruckView.getChildren().get(5);
        assertTrue(msgLabel.getText().contains("Недостатньо місця"));
    }

    @Test
    public void testLoadWithoutSelectingCoffee() {
        interact(() -> {
            ((TextField) loadTruckView.getChildren().get(3)).setText("1");
            ((Button) loadTruckView.getChildren().get(4)).fire();
        });

        Label msgLabel = (Label) loadTruckView.getChildren().get(5);
        assertTrue(msgLabel.getText().contains("оберіть товар"));
    }

    @Test
    public void testLoadInvalidQuantity() {
        Coffee coffee = new Coffee("Arabica", "в зернах", 100, 500, 0.5);
        when(coffeeService.getAllCoffees()).thenReturn(List.of(coffee));

        interact(() -> {
            ((TextField) loadTruckView.getChildren().get(0)).setText("100");
            ((Button) loadTruckView.getChildren().get(1)).fire();

            ListView<String> listView = (ListView<String>) loadTruckView.getChildren().get(2);
            listView.getSelectionModel().select(0);

            ((TextField) loadTruckView.getChildren().get(3)).setText("abc");
            ((Button) loadTruckView.getChildren().get(4)).fire();
        });

        Label msgLabel = (Label) loadTruckView.getChildren().get(5);
        assertTrue(msgLabel.getText().contains("Неправильний формат кількості"));
    }
}
