package view;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.TruckCoffeeDAO;
import database.TruckDAO;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import model.Coffee;
import model.Truck;
import service.CoffeeService;

public class LoadedCoffeeView extends VBox {

    private static final Logger logger = LoggerFactory.getLogger(LoadedCoffeeView.class);
    private final TruckDAO truckDAO;
    private final TruckCoffeeDAO coffeeDAO;

    protected TruckDAO createTruckDAO() {
        return new TruckDAO();
    }

    protected TruckCoffeeDAO createTruckCoffeeDAO() {
        return new TruckCoffeeDAO();
    }

    public LoadedCoffeeView(CoffeeService coffeeService) {
        setSpacing(10);

        this.truckDAO = createTruckDAO();
        this.coffeeDAO = createTruckCoffeeDAO();

        logger.info("Створення вікна завантажених товарів");

        Label trucksLabel = new Label("Фургони:");
        ListView<Truck> trucksList = new ListView<>();
        trucksList.setPrefHeight(115);

        Label coffeeLabel = new Label("Завантажена кава:");
        ListView<String> coffeeList = new ListView<>();
        coffeeList.setPrefHeight(115);

        try {
            List<Truck> allTrucks = truckDAO.getAllTrucks();
            trucksList.setItems(FXCollections.observableArrayList(allTrucks));
            logger.info("Завантажено {} фургонів", allTrucks.size());
        } catch (Exception e) {
            logger.error("Помилка при завантаженні фургонів", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не вдалося завантажити список фургонів.");
            alert.showAndWait();
        }

        trucksList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedTruck) -> {
            coffeeList.getItems().clear();
            if (selectedTruck != null) {
                try {
                    Map<Coffee, Integer> coffeeMap = coffeeDAO.getCoffeeForTruck(selectedTruck.getId());
                    logger.info("Обрано фургон ID={} із {} видами кави", selectedTruck.getId(), coffeeMap.size());
                    if (coffeeMap.isEmpty()) {
                        coffeeList.getItems().add("Немає кави у цьому фургоні.");
                    } else {
                        coffeeMap.forEach((coffee, qty) -> {
                            coffeeList.getItems().add(coffee + ", Кількість: " + qty);
                        });
                    }
                } catch (Exception e) {
                    logger.error("Помилка при завантаженні кави для фургона ID={}", selectedTruck.getId(), e);
                    coffeeList.getItems().add("Не вдалося завантажити каву.");
                }
            }
        });

        Button deleteBtn = new Button("Видалити фургон");

        deleteBtn.setOnAction(e -> {
            Truck selectedTruck = trucksList.getSelectionModel().getSelectedItem();
            if (selectedTruck != null) {
                try {
                    boolean success = truckDAO.deleteTruckById(selectedTruck.getId());
                    if (success) {
                        trucksList.getItems().remove(selectedTruck);
                        coffeeList.getItems().clear();
                        logger.info("Фургон ID={} успішно видалено", selectedTruck.getId());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Фургон успішно видалено.");
                        alert.showAndWait();
                    } else {
                        logger.warn("Не вдалося видалити фургон ID={}", selectedTruck.getId());
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Не вдалося видалити фургон.");
                        alert.showAndWait();
                    }
                } catch (Exception ex) {
                    logger.error("Помилка при видаленні фургона ID={}", selectedTruck.getId(), ex);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Помилка при видаленні фургона.");
                    alert.showAndWait();
                }
            }
        });

        getChildren().addAll(trucksLabel, trucksList, coffeeLabel, coffeeList, deleteBtn);
    }
}
