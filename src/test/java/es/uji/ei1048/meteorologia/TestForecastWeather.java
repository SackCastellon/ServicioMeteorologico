package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.Coordinates;
import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class TestForecastWeather {

    private static IWeatherService api;

    @BeforeAll
    static void setup() {
        api = new OpenWeather();
    }

    @Test
    void getForecastWeather_validCity_suc() {
        final @NotNull City city = new City(-1, "Castellón de la Plana", "Spain", new Coordinates(-1.0, -1.0));
        Assertions.assertDoesNotThrow(() -> api.getForecast(city, 5));
    }

    @Test
    void getForecastWeather_notValidCity_err() {
        final @NotNull City city = new City(-1, "Wakanda", "Yupilandia", new Coordinates(-1.0, -1.0));
        Assertions.assertThrows(NotFoundException.class, () -> api.getForecast(city, 5));
    }
}
