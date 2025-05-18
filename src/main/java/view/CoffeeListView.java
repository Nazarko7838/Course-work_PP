package view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import service.CoffeeService;

public class CoffeeListView extends VBox {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeListView.class);

    public CoffeeListView(CoffeeService coffeeService) {
        logger.info("Створення вікна списку кави");

        ListView<String> listView = new ListView<>();
        try {
            var coffees = coffeeService.getAllCoffees();
            coffees.forEach(coffee -> listView.getItems().add(coffee.toString()));
            logger.info("Успішно завантажено {} товарів", coffees.size());
        } catch (Exception e) {
            logger.error("Помилка при завантаженні списку кави", e);
        }

        this.getChildren().add(listView);
        listView.setPrefHeight(270);
    }
}
