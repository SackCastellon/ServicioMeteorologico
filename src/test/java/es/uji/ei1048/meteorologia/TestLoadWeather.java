package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class TestLoadWeather {

    private static WeatherManager manager;

    @BeforeAll
    static void setUp() {
        manager = WeatherManager.getInstance();
    }

    @Test
    void getLoadWeather_validCity_suc() {
        final SearchPane controller = new SearchPane();
        controller.managerProperty().set(manager);

        final @NotNull String query = "Madrid (ES)";
        assertDoesNotThrow(() -> manager.load(query));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        final SearchPane controller = new SearchPane();
        controller.managerProperty().set(manager);

        final @NotNull String query = "Wakanda";
        assertThrows(IllegalArgumentException.class, () -> manager.load(query));
    }

}
