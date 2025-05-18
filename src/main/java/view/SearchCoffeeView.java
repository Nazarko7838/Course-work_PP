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

public class SearchCoffeeView extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SearchCoffeeView.class);

    public SearchCoffeeView(CoffeeService coffeeService) {
        setPadding(new Insets(5));
        setSpacing(5);

        TextField minWeightField = new TextField();
        minWeightField.setPromptText("Мін. вага");

        TextField maxWeightField = new TextField();
        maxWeightField.setPromptText("Макс. вага");

        TextField minPriceField = new TextField();
        minPriceField.setPromptText("Мін. ціна");

        TextField maxPriceField = new TextField();
        maxPriceField.setPromptText("Макс. ціна");

        Button searchButton = new Button("Пошук");
        ListView<String> resultView = new ListView<>();
        resultView.setPrefHeight(130);

        Label statusLabel = new Label();

        searchButton.setOnAction(e -> {
            try {
                double minW = Double.parseDouble(minWeightField.getText());
                double maxW = Double.parseDouble(maxWeightField.getText());
                double minP = Double.parseDouble(minPriceField.getText());
                double maxP = Double.parseDouble(maxPriceField.getText());

                logger.info("Пошук кави за параметрами: вага від {} до {}, ціна від {} до {}", minW, maxW, minP, maxP);

                resultView.getItems().clear();
                for (Coffee c : coffeeService.findByQuality(minW, maxW, minP, maxP)) {
                    resultView.getItems().add(c.toString());
                }

                statusLabel.setText("Знайдено " + resultView.getItems().size() + " позицій.");
                statusLabel.setStyle("-fx-text-fill: green;");
            } catch (NumberFormatException ex) {
                logger.warn("Некоректний формат введених даних для пошуку кави: {}", ex.getMessage());
                resultView.getItems().clear();
                resultView.getItems().add("Помилка у введенні даних.");
                statusLabel.setText("Будь ласка, введіть коректні числові значення.");
                statusLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                logger.error("Критична помилка при пошуку кави", ex);
                resultView.getItems().clear();
                resultView.getItems().add("Критична помилка. Зверніться до адміністратора.");
                statusLabel.setText("Помилка пошуку. Зверніться до адміністратора.");
                statusLabel.setStyle("-fx-text-fill: red;");

                // Можна додати відправку email, якщо потрібно
                // EmailNotifier.sendErrorNotification("Критична помилка у SearchCoffeeView", ex);
            }
        });

        getChildren().addAll(minWeightField, maxWeightField, minPriceField, maxPriceField, searchButton, resultView, statusLabel);
    }
}
