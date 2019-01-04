package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.service.NotFoundException;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

        final @NotNull String file = "Madrid.txt";
        Assertions.assertDoesNotThrow(() -> manager.load(file));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        final SearchPane controller = new SearchPane();
        controller.managerProperty().set(manager);

        final @NotNull String file = "Wakanda.txt";
        Assertions.assertThrows(NotFoundException.class, () -> manager.load(file));
    }

}
