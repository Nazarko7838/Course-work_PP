package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Coffee;
import service.CoffeeService;

public class AddCoffeeViewTest extends ApplicationTest {

    private CoffeeService coffeeService;
    private AddCoffeeView view;

    @BeforeEach
    public void setUp() {
        coffeeService = mock(CoffeeService.class);
        view = new AddCoffeeView(coffeeService);
    }

    @Test
    public void testAddCoffeeView_CorrectInput_AddsCoffee() {
        // Імітуємо вибір типу кави і пакування
        ComboBox<String> typeBox = (ComboBox<String>) view.getChildren().get(1);
        ComboBox<String> packagingBox = (ComboBox<String>) view.getChildren().get(3);

        TextField volumeField = (TextField) view.getChildren().get(4);
        TextField weightField = (TextField) view.getChildren().get(5);
        TextField priceField = (TextField) view.getChildren().get(6);

        Button addButton = (Button) view.getChildren().get(7);
        Label statusLabel = (Label) view.getChildren().get(8);

        // Встановлюємо значення
        interact(() -> {
            typeBox.setValue("Зернова");
            packagingBox.setValue("Банка");
            volumeField.setText("1.0");
            weightField.setText("2.0");
            priceField.setText("150.0");
            addButton.fire();
        });

        verify(coffeeService, times(1)).addCoffee(any(Coffee.class));
        assertEquals("Каву додано!", statusLabel.getText());
    }

    @Test
    public void testAddCoffeeView_InvalidInput_ShowsError() {
        ComboBox<String> typeBox = (ComboBox<String>) view.getChildren().get(1);
        ComboBox<String> packagingBox = (ComboBox<String>) view.getChildren().get(3);
        TextField volumeField = (TextField) view.getChildren().get(4);
        TextField weightField = (TextField) view.getChildren().get(5);
        TextField priceField = (TextField) view.getChildren().get(6);
        Button addButton = (Button) view.getChildren().get(7);
        Label statusLabel = (Label) view.getChildren().get(8);

        // Встановлюємо неправильні дані
        interact(() -> {
            typeBox.setValue("Зернова");
            packagingBox.setValue("Банка");
            volumeField.setText("bad_number"); // Неправильний формат
            weightField.setText("2.0");
            priceField.setText("150.0");
            addButton.fire();
        });

        verify(coffeeService, never()).addCoffee(any(Coffee.class));
        assertEquals("Помилка при введенні даних.", statusLabel.getText());
    }

    @Test
    public void testAddCoffeeView_VolumeAdjustedCorrectly() {
        ComboBox<String> typeBox = (ComboBox<String>) view.getChildren().get(1);
        ComboBox<String> packagingBox = (ComboBox<String>) view.getChildren().get(3);
        TextField volumeField = (TextField) view.getChildren().get(4);
        TextField weightField = (TextField) view.getChildren().get(5);
        TextField priceField = (TextField) view.getChildren().get(6);
        Button addButton = (Button) view.getChildren().get(7);

        interact(() -> {
            typeBox.setValue("Розчинна");
            packagingBox.setValue("Пачка"); // +0.05 до об'єму
            volumeField.setText("1.0");
            weightField.setText("1.0");
            priceField.setText("100.0");
            addButton.fire();
        });

        verify(coffeeService).addCoffee(argThat(coffee -> coffee.getUnitVolume() == 1.05));
    }
}
