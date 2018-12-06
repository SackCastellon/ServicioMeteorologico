package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.api.NotFoundException;
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
        final @NotNull String city = "Castellón de la Plana";
        Assertions.assertDoesNotThrow(() -> api.getForecast(city, 5));
    }

    @Test
    void getForecastWeather_notValidCity_err() {
        final @NotNull String city = "Wakanda";
        Assertions.assertThrows(NotFoundException.class, () -> api.getForecast(city, 5));
    }
}
