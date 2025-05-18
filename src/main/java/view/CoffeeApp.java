package view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.CoffeeService;

public class CoffeeApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeApp.class);
    private CoffeeService coffeeService = new CoffeeService();


    protected void setCoffeeService(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Запуск програми CoffeeApp");

        try {
            DatabaseManager.initializeDatabase();
            logger.info("Базу даних ініціалізовано успішно");
        } catch (Exception e) {
            logger.error("Помилка при ініціалізації бази даних", e);
        }

        try {
            // Іконка програми
            Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            logger.warn("Не вдалося завантажити іконку: /icon.png", e);
        }

        primaryStage.setTitle("Фургон з Кавою");

        // === Основний макет ===
        BorderPane root = new BorderPane();

        // === Ліва панель ===
        BorderPane leftPanel = new BorderPane();
        leftPanel.setPadding(new Insets(15));
        leftPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6);");

        VBox topButtons = new VBox(10);
        Button btnView = new Button("Переглянути товари");
        Button btnAdd = new Button("Додати каву");
        Button btnLoad = new Button("Завантажити фургон");
        Button btnSort = new Button("Сортувати");
        Button btnSearch = new Button("Пошук");
        Button btnLoaded = new Button("Завантажені товари");

        for (Button b : new Button[] { btnView, btnAdd, btnLoad, btnSort, btnSearch, btnLoaded }) {
            b.setMaxWidth(Double.MAX_VALUE);
        }

        topButtons.getChildren().addAll(btnView, btnAdd, btnLoad, btnSort, btnSearch, btnLoaded);

        // Кнопка "Вихід"
        Button btnExit = new Button("Вихід");
        btnExit.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnExit.setMaxWidth(Double.MAX_VALUE);
        btnExit.setOnAction(e -> {
            logger.info("Програма завершена користувачем");
            primaryStage.close();
        });

        leftPanel.setTop(topButtons);
        leftPanel.setBottom(btnExit);

        // === Центральна панель ===
        StackPane contentPane = new StackPane();
        contentPane.setPadding(new Insets(15));

        // === Фонове зображення ===
        try {
            Image backgroundImage = new Image("file:src/main/resources/bg2.jpg");
            BackgroundImage background = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
            root.setBackground(new Background(background));
            logger.info("Фонове зображення завантажено успішно");
        } catch (Exception e) {
            logger.error("Не вдалося завантажити фонове зображення", e);
        }

        // Додаємо панелі
        root.setLeft(leftPanel);
        root.setCenter(contentPane);

        // === Дії кнопок ===
        btnView.setOnAction(e -> {
            contentPane.getChildren().setAll(new CoffeeListView(coffeeService));
            logger.info("Перегляд товарів відкрито");
        });

        btnAdd.setOnAction(e -> {
            contentPane.getChildren().setAll(new AddCoffeeView(coffeeService));
            logger.info("Форма додавання кави відкрита");
        });

        btnLoad.setOnAction(e -> {
            contentPane.getChildren().setAll(new LoadTruckView(coffeeService));
            logger.info("Завантаження фургону відкрите");
        });

        btnSort.setOnAction(e -> {
            contentPane.getChildren().setAll(new SortCoffeeView(coffeeService));
            logger.info("Сортування кави відкрите");
        });

        btnSearch.setOnAction(e -> {
            contentPane.getChildren().setAll(new SearchCoffeeView(coffeeService));
            logger.info("Пошук кави відкрито");
        });

        btnLoaded.setOnAction(e -> {
            contentPane.getChildren().setAll(new LoadedCoffeeView(coffeeService));
            logger.info("Перегляд завантажених товарів відкрито");
        });

        // === Початковий екран ===
        contentPane.getChildren().add(new CoffeeListView(coffeeService));
        logger.info("Початковий екран завантажено");

        // === Встановлення сцени ===
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        logger.info("Головне вікно показано");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
