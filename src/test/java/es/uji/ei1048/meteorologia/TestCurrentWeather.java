package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.service.IWeatherProvider;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class TestCurrentWeather {

    private static IWeatherProvider service;

    @BeforeAll
    static void setUp() {
        service = OpenWeather.getInstance();
    }

    @Test
    void getCurrentWeather_validCity_suc() {
        final @NotNull SearchPane controller = new SearchPane();
        controller.providerProperty().set(service);

        final @NotNull City validCity = new City(6359304L, "Madrid", "ES");
        Assertions.assertNotNull(controller.getWeather(validCity));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        final @NotNull SearchPane controller = new SearchPane();
        controller.providerProperty().set(service);

        final @NotNull City invalidCity = new City(-1L, "Wakanda", "XX");
        Assertions.assertThrows(NotFoundException.class, () -> controller.getWeather(invalidCity));
    }
}
