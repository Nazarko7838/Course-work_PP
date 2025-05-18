package view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Coffee;
import service.CoffeeService;

public class LoadTruckView extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(LoadTruckView.class);

    public LoadTruckView(CoffeeService coffeeService) {
        setPadding(new Insets(10));
        setSpacing(10);

        TextField capacityField = new TextField();
        capacityField.setPromptText("Введіть обʼєм фургона (л)");

        Button initTruckBtn = new Button("Створити фургон");

        ListView<String> coffeeList = new ListView<>();
        coffeeList.setPrefHeight(170);

        TextField quantityField = new TextField();
        quantityField.setPromptText("Кількість для завантаження");

        Button loadBtn = new Button("Завантажити обране");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: green;"); // Колір для успішного повідомлення

        initTruckBtn.setOnAction(e -> {
            try {
                double capacity = Double.parseDouble(capacityField.getText());
                coffeeService.createTruck(capacity);
                coffeeList.getItems().clear();
                for (Coffee c : coffeeService.getAllCoffees()) {
                    coffeeList.getItems().add(c.toString());
                }
                messageLabel.setText("Фургон успішно створено з обʼємом " + capacity + " л.");
                messageLabel.setStyle("-fx-text-fill: green;");
                logger.info("Фургон створено з об'ємом {} л", capacity);
            } catch (NumberFormatException ex) {
                messageLabel.setText("❌ Неправильний формат обʼєму фургона.");
                messageLabel.setStyle("-fx-text-fill: red;");
                logger.warn("Спроба створити фургон з неправильним форматом об'єму: {}", capacityField.getText());
            } catch (Exception ex) {
                messageLabel.setText("❌ Помилка створення фургона. Перевірте введені дані.");
                messageLabel.setStyle("-fx-text-fill: red;");
                logger.error("Помилка створення фургона", ex);
            }
        });

        loadBtn.setOnAction(e -> {
            int index = coffeeList.getSelectionModel().getSelectedIndex();
            try {
                if (index < 0) {
                    messageLabel.setText("❌ Будь ласка, оберіть товар для завантаження.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    logger.warn("Не обрано товар для завантаження");
                    return;
                }
                int quantity = Integer.parseInt(quantityField.getText());
                Coffee selected = coffeeService.getAllCoffees().get(index);
                boolean success = coffeeService.loadTruckWithCoffee(selected, quantity);
                if (success) {
                    messageLabel.setText("✅ Успішно завантажено " + quantity + " од. " + selected.getCoffeeType() + " кава");
                    messageLabel.setStyle("-fx-text-fill: green;");
                    logger.info("Завантажено {} од. кави '{}' у фургон", quantity, selected.getCoffeeType());
                } else {
                    messageLabel.setText("❌ Недостатньо місця у фургоні для завантаження.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    logger.warn("Недостатньо місця у фургоні для завантаження {} од. кави '{}'", quantity, selected.getCoffeeType());
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("❌ Неправильний формат кількості.");
                messageLabel.setStyle("-fx-text-fill: red;");
                logger.warn("Неправильний формат кількості: {}", quantityField.getText());
            } catch (Exception ex) {
                messageLabel.setText("❌ Помилка завантаження.");
                messageLabel.setStyle("-fx-text-fill: red;");
                logger.error("Помилка завантаження кави у фургон", ex);
            }
        });

        getChildren().addAll(capacityField, initTruckBtn, coffeeList, quantityField, loadBtn, messageLabel);
    }
}
