package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.*;
import es.uji.ei1048.meteorologia.view.ResultsPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

final class TestSaveWeather {

    private static WeatherManager manager;

    @BeforeAll
    static void setUp() {
        manager = WeatherManager.getInstance();
    }

    @Test
    void getSaveWeather_validSave_suc() {
        final ResultsPane controller = new ResultsPane();
        controller.managerProperty().set(manager);

        final @NotNull City city = new City(6359304L, "Zaragoza", "ES");
        final @NotNull WeatherData wd = new WeatherData(
                city,
                LocalDateTime.now(),
                new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0),
                10.0,
                10.0
        );

        Assertions.assertTrue(manager.save(wd));
    }

    @Test
    void getSaveWeather_notValidSave_err() {
        final ResultsPane controller = new ResultsPane();
        controller.managerProperty().set(manager);

        final @NotNull City city = new City(6359304L, "Madrid", "ES");
        final @NotNull WeatherData wd = new WeatherData(
                city,
                LocalDateTime.now(),
                new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0),
                10.0,
                10.0
        );

        Assertions.assertFalse(manager.save(wd));
    }

}
