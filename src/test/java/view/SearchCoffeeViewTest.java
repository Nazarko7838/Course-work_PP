package view;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Coffee;
import service.CoffeeService;

public class SearchCoffeeViewTest extends ApplicationTest {

    private CoffeeService mockService;
    private SearchCoffeeView view;

    private TextField minWeightField;
    private TextField maxWeightField;
    private TextField minPriceField;
    private TextField maxPriceField;
    private Button searchButton;
    private ListView<String> resultView;
    private Label statusLabel;

    @BeforeEach
    public void setUp() {
        mockService = mock(CoffeeService.class);
        view = new SearchCoffeeView(mockService);

        minWeightField = (TextField) view.getChildren().get(0);
        maxWeightField = (TextField) view.getChildren().get(1);
        minPriceField = (TextField) view.getChildren().get(2);
        maxPriceField = (TextField) view.getChildren().get(3);
        searchButton = (Button) view.getChildren().get(4);
        resultView = (ListView<String>) view.getChildren().get(5);
        statusLabel = (Label) view.getChildren().get(6);
    }

    @Test
    public void testSuccessfulSearch() {
        Coffee c1 = new Coffee("Зернова","Банка", 200, 100, 30.0);
        Coffee c2 = new Coffee("Мелена","Пачка", 250, 80, 25.0);
        when(mockService.findByQuality(100, 300, 20, 35)).thenReturn(Arrays.asList(c1, c2));

        minWeightField.setText("100");
        maxWeightField.setText("300");
        minPriceField.setText("20");
        maxPriceField.setText("35");

        searchButton.fire();

        assertEquals(2, resultView.getItems().size());
        assertTrue(resultView.getItems().get(0).contains("Зернова"));
        assertTrue(statusLabel.getText().contains("2 позицій"));
        assertEquals("green", statusLabel.getStyle().contains("green") ? "green" : "not green");
    }

    @Test
    public void testSearchWithInvalidNumberFormat() {
        minWeightField.setText("a");
        maxWeightField.setText("b");
        minPriceField.setText("c");
        maxPriceField.setText("d");

        searchButton.fire();

        assertEquals(1, resultView.getItems().size());
        assertEquals("Помилка у введенні даних.", resultView.getItems().get(0));
        assertTrue(statusLabel.getText().contains("коректні числові"));
        assertEquals("red", statusLabel.getStyle().contains("red") ? "red" : "not red");
    }

    @Test
    public void testSearchWithExceptionInService() {
        when(mockService.findByQuality(10, 20, 5, 15)).thenThrow(new RuntimeException("DB Error"));

        minWeightField.setText("10");
        maxWeightField.setText("20");
        minPriceField.setText("5");
        maxPriceField.setText("15");

        searchButton.fire();

        assertEquals(1, resultView.getItems().size());
        assertEquals("Критична помилка. Зверніться до адміністратора.", resultView.getItems().get(0));
        assertTrue(statusLabel.getText().contains("Зверніться до адміністратора"));
        assertEquals("red", statusLabel.getStyle().contains("red") ? "red" : "not red");
    }

    @Test
    public void testSearchWithNoResults() {
        when(mockService.findByQuality(10, 20, 5, 15)).thenReturn(Collections.emptyList());

        minWeightField.setText("10");
        maxWeightField.setText("20");
        minPriceField.setText("5");
        maxPriceField.setText("15");

        searchButton.fire();

        assertEquals(0, resultView.getItems().size());
        assertTrue(statusLabel.getText().contains("0 позицій"));
    }
}
