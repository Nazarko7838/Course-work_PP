package view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Coffee;
import service.CoffeeService;

public class AddCoffeeView extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(AddCoffeeView.class);

    public AddCoffeeView(CoffeeService coffeeService) {
        setPadding(new Insets(10));
        setSpacing(10);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Зернова", "Мелена", "Розчинна");

        ComboBox<String> packagingBox = new ComboBox<>();
        packagingBox.getItems().addAll("Банка", "Пачка");

        TextField volumeField = new TextField();
        volumeField.setPromptText("Об'єм (л)");

        TextField weightField = new TextField();
        weightField.setPromptText("Вага (кг)");

        TextField priceField = new TextField();
        priceField.setPromptText("Ціна (грн/кг)");

        Button addButton = new Button("Додати каву");
        Label status = new Label();

        addButton.setOnAction(e -> {
            try {
                String type = typeBox.getValue();
                String packaging = packagingBox.getValue();
                double volume = Double.parseDouble(volumeField.getText());
                double weight = Double.parseDouble(weightField.getText());
                double price = Double.parseDouble(priceField.getText());

                // Додаємо обʼєм тари
                volume += packaging.equals("Банка") ? 0.1 : 0.05;

                Coffee coffee = new Coffee(type, packaging, volume, weight, price);
                coffeeService.addCoffee(coffee);

                logger.info("Кава успішно додана: {}", coffee);
                status.setText("Каву додано!");
            } catch (Exception ex) {
                logger.error("Помилка при додаванні кави", ex);
                status.setText("Помилка при введенні даних.");
            }
        });

        getChildren().addAll(new Label("Тип кави:"), typeBox,
                             new Label("Тип пакування:"), packagingBox,
                             volumeField, weightField, priceField,
                             addButton, status);
    }
}
