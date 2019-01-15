package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.service.IWeatherProvider;
import es.uji.ei1048.meteorologia.service.NotFoundException;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class TestCurrentWeather {

    private static IWeatherProvider service;
    private static SearchPane controller;

    @BeforeAll
    static void setUp() {
        service = OpenWeather.getInstance();
        controller = new SearchPane();
    }

    @Test
    void getCurrentWeather_validCity_suc() {
        controller.providerProperty().set(service);
        final @NotNull City validCity = new City(6359304L, "Madrid", "ES");
        assertNotNull(controller.getWeather(validCity));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        controller.providerProperty().set(service);
        final @NotNull City invalidCity = new City(-1L, "Wakanda", "MCU");
        assertThrows(NotFoundException.class, () -> controller.getWeather(invalidCity));
    }
}
