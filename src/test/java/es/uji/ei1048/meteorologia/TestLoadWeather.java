package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.*;
import es.uji.ei1048.meteorologia.view.LoadWeather;
import es.uji.ei1048.meteorologia.view.ResultsPane;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class TestLoadWeather {


    private static LoadWeather controller;

    @BeforeAll
    static void setUp() {

        controller = new LoadWeather();

        WeatherManager manager = WeatherManager.getInstance();
        manager.deleteAll();
        ResultsPane resultsPane = new ResultsPane();
        resultsPane.managerProperty().set(manager);

        resultsPane.save(new WeatherData(
                new City(6359304, "Madrid", "ES"),
                LocalDateTime.now(),
                new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0),
                10.0,
                10.0));
    }

    @Test
    void getLoadWeather_validCity_suc() {
        final @NotNull String query = "Madrid (ES)";
        assertDoesNotThrow(() -> controller.getSavedCities(query));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        final @NotNull String query = "Wakanda";
        assertThrows(IllegalArgumentException.class, () -> controller.getSavedCities(query));
    }

}
