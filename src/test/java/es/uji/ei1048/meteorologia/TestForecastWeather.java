package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.IWeatherApi;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.api.OpenWeatherApi;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestForecastWeather {

    private static IWeatherApi api;

    @BeforeAll
    public static void setup() {
        api = new OpenWeatherApi();
    }

    @Test
    public void getForecastWeather_validCity_suc() {
        final @NotNull String city = "Castellón de la Plana";
        Assertions.assertDoesNotThrow(() -> api.getForecast(city, 5));
    }

    @Test
    public void getForecastWeather_notValidCity_err() {
        final @NotNull String city = "Wakanda";
        Assertions.assertThrows(NotFoundException.class, () -> api.getForecast(city, 5));
    }

}
