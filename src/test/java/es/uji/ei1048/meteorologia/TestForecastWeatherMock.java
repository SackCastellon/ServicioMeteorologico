package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TestForecastWeatherMock {

    @Mock
    private IWeatherService api;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getForecastWeather_validCity_suc() throws Exception {
        final @NotNull String city = "Castellón de la Plana";
        when(api.getForecast(anyString(), anyInt())).thenReturn(anyList());
        Assertions.assertDoesNotThrow(() -> api.getForecast(city, 5));
    }

    @Test
    void geForecastWeather_notValidCity_err() throws Exception {
        final @NotNull String city = "Wakanda";
        when(api.getForecast(any(String.class), any(Integer.class))).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> api.getForecast(city, 5));
    }
}
