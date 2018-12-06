package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.Coordinates;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.service.IWeatherService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

final class TestCurrentWeatherMock {

    @Mock
    private IWeatherService api;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getCurrentWeather_validCity_suc() {
        final @NotNull City city = new City(-1, "Castellón de la Plana", "Spain", new Coordinates(-1.0, -1.0));
        when(api.getWeather(any(City.class))).thenReturn(any(WeatherData.class));
        Assertions.assertDoesNotThrow(() -> api.getWeather(city));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        final @NotNull City city = new City(-1, "Wakanda", "Yupilandia", new Coordinates(-1.0, -1.0));
        when(api.getWeather(any(City.class))).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> api.getWeather(city));
    }
}
