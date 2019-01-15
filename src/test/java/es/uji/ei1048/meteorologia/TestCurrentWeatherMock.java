package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.service.IWeatherProvider;
import es.uji.ei1048.meteorologia.service.NotFoundException;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class TestCurrentWeatherMock {



    @Mock
    private SearchPane controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void getCurrentWeather_validCity_suc() {
        final @NotNull WeatherData data = mock(WeatherData.class);
        when(controller.getWeather(any(City.class))).thenReturn(data);
        final @NotNull City validCity = new City(6359304L, "Madrid", "ES");
        assertNotNull(controller.getWeather(validCity));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        when(controller.getWeather(any(City.class))).thenThrow(NotFoundException.class);
        final @NotNull City invalidCity = new City(-1L, "Wakanda", "MCU");
        assertThrows(NotFoundException.class, () -> controller.getWeather(invalidCity));
    }
}