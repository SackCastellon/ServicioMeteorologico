package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class TestCurrentWeather {

    private static IWeatherService api;

    @BeforeAll
    static void setup() {
        api = new OpenWeather();
    }

    @Test
    void getCurrentWeather_validCity_suc() {
        final @NotNull String city = "Castellón de la Plana";
        Assertions.assertDoesNotThrow(() -> api.getWeather(city));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        final @NotNull String city = "Wakanda";
        Assertions.assertThrows(NotFoundException.class, () -> api.getWeather(city));
    }
}
