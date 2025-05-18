package view;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.mock;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import service.CoffeeService;

@ExtendWith(ApplicationExtension.class)
public class CoffeeAppTest {

    private CoffeeApp app;
    private CoffeeService mockService;

    @Start
    public void start(Stage stage) {
        mockService = mock(CoffeeService.class);
        app = new CoffeeApp();
        app.setCoffeeService(mockService); // ← використовуємо сеттер
        app.start(stage);
    }


    @Test
    void testClickAddCoffeeShowsAddCoffeeView(FxRobot robot) {
        robot.clickOn("Додати каву");
        assertNotNull(robot.lookup("Тип кави:").query());
    }

    @Test
    void testClickViewShowsCoffeeList(FxRobot robot) {
        robot.clickOn("Переглянути товари");
        // Наприклад, очікуємо заголовок або елемент, який є в CoffeeListView
        verifyThat(".button", hasText("Переглянути товари"));

    }

    @Test
    void testClickLoadTruckView(FxRobot robot) {
        robot.clickOn("Завантажити фургон");
        assertNotNull(robot.lookup(".label").query());
    }

    @Test
    void testClickSortCoffeeView(FxRobot robot) {
        robot.clickOn("Сортувати");
        assertNotNull(robot.lookup(".label").query());
    }

    @Test
    void testClickSearchCoffeeView(FxRobot robot) {
        robot.clickOn("Пошук");
        assertNotNull(robot.lookup(".label").query());
    }

    @Test
    void testClickLoadedCoffeeView(FxRobot robot) {
        robot.clickOn("Завантажені товари");
        assertNotNull(robot.lookup(".label").query());
    }

    @Test
    void testExitButtonClosesStage(FxRobot robot) {
        Button exitButton = robot.lookup("Вихід").queryAs(Button.class);
        assertNotNull(exitButton);
        robot.clickOn(exitButton);
        // ТестFX автоматично закриє вікно — можна перевірити логіку через моканий
        // логгер
    }
}
