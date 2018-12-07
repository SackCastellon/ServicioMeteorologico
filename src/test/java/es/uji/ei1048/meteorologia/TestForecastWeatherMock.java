package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.Coordinates;
import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.view.SearchPane;
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
    private SearchPane searchPane;
    @Mock
    private IWeatherService api;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        searchPane.setApi(api);
    }

    @Test
    void getForecastWeather_validCity_suc() throws Exception {
        final @NotNull String city = "Madrid";
        when(api.getForecast(any(String.class), anyInt())).thenReturn(anyList());
        Assertions.assertDoesNotThrow(() -> searchPane.getForecast(city,3));
    }

    @Test
    void geForecastWeather_notValidCity_err() throws Exception {
        final @NotNull String city = "Wakanda";
        when(api.getForecast(any(String.class), anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () ->  searchPane.getForecast(city,3));
    }
}
