package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class TestForecastWeather {

    private SearchPane searchPane;


    @BeforeEach
    void setup() {
        searchPane = new SearchPane();
        searchPane.setApi(new OpenWeather());
    }

    @Test
    void getForecastWeather_validCity_suc() {
        final @NotNull String city = "Madrid";
        Assertions.assertDoesNotThrow(() -> searchPane.getForecast(city,3));
    }

    @Test
    void getForecastWeather_notValidCity_err() {
        final @NotNull String city = "Wakanda";
        Assertions.assertThrows(NotFoundException.class, () -> searchPane.getForecast(city, 3));
    }
}
