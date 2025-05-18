package view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javafx.scene.control.ListView;
import model.Coffee;
import service.CoffeeService;

class CoffeeListViewTest {

    private CoffeeService mockService;

    @BeforeEach
    void setup() {
        mockService = mock(CoffeeService.class);
    }

    @Test
    void testCoffeeListViewLoadsItems() {
        // Arrange
        Coffee coffee1 = new Coffee("Арабіка", "в зернах", 100.0, 0.5, 1);
        Coffee coffee2 = new Coffee("Робуста", "мелена", 90.0, 0.25, 2);
        List<Coffee> fakeCoffees = Arrays.asList(coffee1, coffee2);

        when(mockService.getAllCoffees()).thenReturn(fakeCoffees);

        // Act
        CoffeeListView view = new CoffeeListView(mockService);

        // Assert
        assertEquals(1, view.getChildren().size());
        assertTrue(view.getChildren().get(0) instanceof ListView);
        ListView<?> listView = (ListView<?>) view.getChildren().get(0);

        assertEquals(2, listView.getItems().size());
        assertEquals(coffee1.toString(), listView.getItems().get(0));
        assertEquals(coffee2.toString(), listView.getItems().get(1));
    }

    @Test
    void testCoffeeListViewEmptyList() {
        when(mockService.getAllCoffees()).thenReturn(Collections.emptyList());

        CoffeeListView view = new CoffeeListView(mockService);

        assertEquals(1, view.getChildren().size());
        ListView<?> listView = (ListView<?>) view.getChildren().get(0);
        assertEquals(0, listView.getItems().size());
    }

    @Test
    void testCoffeeListViewHandlesExceptionGracefully() {
        when(mockService.getAllCoffees()).thenThrow(new RuntimeException("DB error"));

        CoffeeListView view = new CoffeeListView(mockService);

        assertEquals(1, view.getChildren().size());
        ListView<?> listView = (ListView<?>) view.getChildren().get(0);
        assertEquals(0, listView.getItems().size()); // Нічого не додається
    }
}
