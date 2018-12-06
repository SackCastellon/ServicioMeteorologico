package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.Coordinates;
import es.uji.ei1048.meteorologia.service.IWeatherService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

final class TestForecastWeatherMock {

    @Mock
    private IWeatherService api;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getForecastWeather_validCity_suc() throws Exception {
        final @NotNull City city = new City(-1, "Castellón de la Plana", "Spain", new Coordinates(-1.0, -1.0));
        when(api.getForecast(any(City.class), anyInt())).thenReturn(anyList());
        Assertions.assertDoesNotThrow(() -> api.getForecast(city, eq(5)));
    }

    @Test
    void geForecastWeather_notValidCity_err() throws Exception {
        final @NotNull City city = new City(-1, "Wakanda", "Yupilandia", new Coordinates(-1.0, -1.0));
        when(api.getForecast(any(City.class), anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> api.getForecast(city, 5));
    }
}
