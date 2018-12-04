package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.IWeatherApi;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.api.OpenWeatherApi;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TestCurrentWeatherMock {

    @Mock
    private static IWeatherApi api;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCurrentWeather_validCity_suc() throws NotFoundException {
        final @NotNull String city = "Castellón de la Plana";
        when(api.getWeather(anyString())).thenReturn(any(WeatherData.class));
        Assertions.assertDoesNotThrow(() -> api.getWeather(city));
    }

    @Test
    public void getCurrentWeather_notValidCity_err() throws NotFoundException {
        final @NotNull String city = "Wakanda";
        when(api.getWeather(any(String.class))).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> api.getWeather(city));
    }
}
