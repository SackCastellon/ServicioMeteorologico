package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestSaveWeatherAdvancedResults {

    private static WeatherManager api;

    @BeforeAll
    static void setUp() {
        api = new WeatherManager();
    }

    @Test
    void getSaveWeather_validSave_suc() {
        final @NotNull WeatherData wd = new WeatherData( new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS), new Wind(20.0, 10.0),
                10.0, 10.0);
        Assertions.assertDoesNotThrow(() -> api.save(wd));
    }

    @Test
    void getSaveWeather_notValidSave_err() {
        final @NotNull WeatherData wd = new WeatherData( new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS), new Wind(20.0, 10.0),
                10.0, 10.0);
        Assertions.assertThrows(NotFoundException.class, () -> api.save(wd));
    }

}
