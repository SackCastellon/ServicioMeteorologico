package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

final class TestCurrentWeatherMock {

    @Mock
    private IWeatherService api;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getCurrentWeather_validCity_suc() throws Exception {
        final @NotNull String city = "Castellón de la Plana";
        when(api.getWeather(anyString())).thenReturn(any(WeatherData.class));
        Assertions.assertDoesNotThrow(() -> api.getWeather(city));
    }

    @Test
    void getCurrentWeather_notValidCity_err() throws Exception {
        final @NotNull String city = "Wakanda";
        when(api.getWeather(any(String.class))).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> api.getWeather(city));
    }
}
