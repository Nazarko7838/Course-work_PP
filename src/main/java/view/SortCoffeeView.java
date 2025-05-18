package view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import service.CoffeeService;

public class SortCoffeeView extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SortCoffeeView.class);

    public SortCoffeeView(CoffeeService coffeeService) {
        ListView<String> listView = new ListView<>();
        listView.setPrefHeight(270);

        try {
            logger.info("Початок сортування кави за ціна/вага");
            coffeeService.sortByPricePerWeight().forEach(coffee -> listView.getItems().add(coffee.toString()));
            logger.info("Сортування завершено, знайдено {} елементів", listView.getItems().size());
        } catch (Exception ex) {
            logger.error("Помилка при сортуванні кави за ціна/вага", ex);
            listView.getItems().clear();
            listView.getItems().add("Помилка при сортуванні даних.");
        }

        this.getChildren().addAll(new Label("Сортовано за ціна/вага:"), listView);
    }
}
