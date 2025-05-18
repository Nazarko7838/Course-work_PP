package view;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Coffee;
import service.CoffeeService;

public class SortCoffeeViewTest extends ApplicationTest {

    private CoffeeService mockService;

    @BeforeEach
    public void setUp() {
        mockService = mock(CoffeeService.class);
    }

    @Test
    public void testSortSuccess() {
        Coffee coffee1 = new Coffee("Мелена","Пачка", 250, 80, 25.0);
        Coffee coffee2 = new Coffee("Зернова","Банка", 200, 100, 30.0);
        when(mockService.sortByPricePerWeight()).thenReturn(Arrays.asList(coffee1, coffee2));

        SortCoffeeView view = new SortCoffeeView(mockService);

        assertEquals(2, view.getChildren().size());

        Label label = (Label) view.getChildren().get(0);
        assertEquals("Сортовано за ціна/вага:", label.getText());

        ListView<String> listView = (ListView<String>) view.getChildren().get(1);
        assertEquals(2, listView.getItems().size());
    }

    @Test
    public void testSortEmptyList() {
        when(mockService.sortByPricePerWeight()).thenReturn(Collections.emptyList());

        SortCoffeeView view = new SortCoffeeView(mockService);
        ListView<String> listView = (ListView<String>) view.getChildren().get(1);
        assertEquals(0, listView.getItems().size());
    }

    @Test
    public void testSortThrowsException() {
        when(mockService.sortByPricePerWeight()).thenThrow(new RuntimeException("DB error"));

        SortCoffeeView view = new SortCoffeeView(mockService);
        ListView<String> listView = (ListView<String>) view.getChildren().get(1);
        assertEquals(1, listView.getItems().size());
        assertEquals("Помилка при сортуванні даних.", listView.getItems().get(0));
    }
}
