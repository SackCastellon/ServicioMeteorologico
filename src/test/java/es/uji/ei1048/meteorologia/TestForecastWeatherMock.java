package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.service.IWeatherProvider;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

final class TestForecastWeatherMock {

    @Mock
    private IWeatherProvider service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getForecastWeather_validCity_suc() {
        when(service.getForecast(any(City.class), anyInt())).thenReturn(anyList());

        final SearchPane controller = new SearchPane();
        controller.providerProperty().set(service);

        final City validCity = new City(6359304L, "Madrid", "ES");
        Assertions.assertNotNull(controller.getForecast(validCity, 3, 1));
    }

    @Test
    void geForecastWeather_notValidCity_err() {
        when(service.getForecast(any(City.class), anyInt())).thenThrow(NotFoundException.class);

        final SearchPane controller = new SearchPane();
        controller.providerProperty().set(service);

        final City invalidCity = new City(-1L, "Wakanda", "XX");
        Assertions.assertThrows(NotFoundException.class, () -> controller.getForecast(invalidCity, 3, 1));
    }
}
