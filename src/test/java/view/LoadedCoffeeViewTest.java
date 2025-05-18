package view;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import database.TruckCoffeeDAO;
import database.TruckDAO;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Coffee;
import model.Truck;
import service.CoffeeService;


@ExtendWith(ApplicationExtension.class)
class LoadedCoffeeViewTest {

    TruckDAO mockTruckDAO;
    TruckCoffeeDAO mockTruckCoffeeDAO;
    CoffeeService mockCoffeeService;
    LoadedCoffeeView view;

    @Start
    private void start(Stage stage) {
        mockTruckDAO = mock(TruckDAO.class);
        mockTruckCoffeeDAO = mock(TruckCoffeeDAO.class);
        mockCoffeeService = mock(CoffeeService.class);

        view = new LoadedCoffeeView(mockCoffeeService) {
            @Override
            protected TruckDAO createTruckDAO() {
                return mockTruckDAO;
            }

            @Override
            protected TruckCoffeeDAO createTruckCoffeeDAO() {
                return mockTruckCoffeeDAO;
            }
        };
        stage.setScene(new javafx.scene.Scene(view));
        stage.show();
    }



    @Test
    void testInitialTruckLoadingException() throws Exception {
        when(mockTruckDAO.getAllTrucks()).thenThrow(new RuntimeException("DB error"));
        Thread.sleep(200);
        @SuppressWarnings("unchecked")
        ListView<Truck> truckListView = (ListView<Truck>) view.getChildren().get(1);
        assertEquals(0, truckListView.getItems().size());
    }

    @Test
    void testTruckSelectionShowsCoffeeList() throws Exception {
        Truck truck = new Truck(80.0);
        Map<Coffee, Integer> coffeeMap = Map.of(new Coffee("Arabica", "Пачка", 250, 80, 25.0), 3);
        when(mockTruckDAO.getAllTrucks()).thenReturn(List.of(truck));
        when(mockTruckCoffeeDAO.getCoffeeForTruck(truck.getId())).thenReturn(coffeeMap);
        Thread.sleep(200);
        @SuppressWarnings("unchecked")
        ListView<Truck> truckListView = (ListView<Truck>) view.getChildren().get(1);
        @SuppressWarnings("unchecked")
        ListView<String> coffeeList = (ListView<String>) view.getChildren().get(3);
        javafx.application.Platform.runLater(() -> truckListView.getSelectionModel().select(truck));
        Thread.sleep(500);
        assertEquals(1, coffeeList.getItems().size());
    }

    @Test
    void testTruckSelectionEmptyCoffee() throws Exception {
        Truck truck = new Truck(90.0);
        when(mockTruckDAO.getAllTrucks()).thenReturn(List.of(truck));
        when(mockTruckCoffeeDAO.getCoffeeForTruck(truck.getId())).thenReturn(Collections.emptyMap());
        Thread.sleep(200);
        @SuppressWarnings("unchecked")
        ListView<Truck> truckListView = (ListView<Truck>) view.getChildren().get(1);
        @SuppressWarnings("unchecked")
        ListView<String> coffeeList = (ListView<String>) view.getChildren().get(3);
        javafx.application.Platform.runLater(() -> truckListView.getSelectionModel().select(truck));
        Thread.sleep(500);
        assertEquals(1, coffeeList.getItems().size());
    }

    @Test
    void testTruckSelectionCoffeeLoadError() throws Exception {
        Truck truck = new Truck(100.0);
        when(mockTruckDAO.getAllTrucks()).thenReturn(List.of(truck));
        when(mockTruckCoffeeDAO.getCoffeeForTruck(truck.getId())).thenThrow(new RuntimeException("DB read fail"));
        Thread.sleep(200);
        @SuppressWarnings("unchecked")
        ListView<Truck> truckListView = (ListView<Truck>) view.getChildren().get(1);
        @SuppressWarnings("unchecked")
        ListView<String> coffeeList = (ListView<String>) view.getChildren().get(3);
        javafx.application.Platform.runLater(() -> truckListView.getSelectionModel().select(truck));
        Thread.sleep(500);
        assertEquals(1, coffeeList.getItems().size());
    }

    @Test
    void testDeleteTruckSuccess() throws Exception {
        Truck truck = new Truck(100.0);
        when(mockTruckDAO.getAllTrucks()).thenReturn(List.of(truck));
        when(mockTruckDAO.deleteTruckById(truck.getId())).thenReturn(true);
        Thread.sleep(200);
        @SuppressWarnings("unchecked")
        ListView<Truck> truckListView = (ListView<Truck>) view.getChildren().get(1);
        javafx.application.Platform.runLater(() -> {
            truckListView.getSelectionModel().select(truck);
            Button deleteBtn = (Button) view.getChildren().get(4);
            deleteBtn.fire();
        });
        Thread.sleep(500);
        assertFalse(truckListView.getItems().contains(truck));
    }

    @Test
    void testDeleteTruckFailure() throws Exception {
        Truck truck = new Truck(100.0);
        when(mockTruckDAO.getAllTrucks()).thenReturn(List.of(truck));
        when(mockTruckDAO.deleteTruckById(truck.getId())).thenReturn(false);
        Thread.sleep(200);
        @SuppressWarnings("unchecked")
        ListView<Truck> truckListView = (ListView<Truck>) view.getChildren().get(1);
        javafx.application.Platform.runLater(() -> {
            truckListView.getSelectionModel().select(truck);
            Button deleteBtn = (Button) view.getChildren().get(4);
            deleteBtn.fire();
        });
        Thread.sleep(500);
        assertFalse(truckListView.getItems().contains(truck));
    }

    @Test
    void testDeleteTruckException() throws Exception {
        Truck truck = new Truck(100.0);
        when(mockTruckDAO.getAllTrucks()).thenReturn(List.of(truck));
        when(mockTruckDAO.deleteTruckById(truck.getId())).thenThrow(new RuntimeException("delete error"));
        Thread.sleep(200);
        @SuppressWarnings("unchecked")
        ListView<Truck> truckListView = (ListView<Truck>) view.getChildren().get(1);
        javafx.application.Platform.runLater(() -> {
            truckListView.getSelectionModel().select(truck);
            Button deleteBtn = (Button) view.getChildren().get(4);
            deleteBtn.fire();
        });
        Thread.sleep(500);
        assertFalse(truckListView.getItems().contains(truck));
    }
}
