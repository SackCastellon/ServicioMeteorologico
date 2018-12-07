package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

final class TestSaveWeatherAdvancedResults {

    private static WeatherManager manager;

    @BeforeAll
    static void setUp() {
        manager =  WeatherManager.INSTANCE;
    }

    @Test
    void getSaveWeather_validSave_suc() {
        final @NotNull WeatherData wd = new WeatherData(
                new City(-1, "Madrid", "España", new Coordinates(0, 0)),
                new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0),
                LocalDate.now());

        Assertions.assertTrue(manager.save(wd));
    }

    @Test
    void getSaveWeather_notValidSave_err() {
        final @NotNull WeatherData wd = new WeatherData(
                new City(-1, "Wakanda", "Africa", new Coordinates(0, 0)),
                new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0),
                LocalDate.now());

        Assertions.assertFalse(manager.save(wd));
    }

}
