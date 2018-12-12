package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.service.IWeatherProvider;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

final class TestCurrentWeatherMock {

    @Mock
    private IWeatherProvider service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getCurrentWeather_validCity_suc() {
        when(service.getWeather(any(City.class))).thenReturn(any(WeatherData.class));

        final SearchPane controller = new SearchPane();
        controller.providerProperty().set(service);

        final City validCity = new City(6359304L, "Madrid", "ES");
        Assertions.assertNotNull(controller.getWeather(validCity));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        when(service.getWeather(any(City.class))).thenThrow(NotFoundException.class);

        final SearchPane controller = new SearchPane();
        controller.providerProperty().set(service);

        final City invalidCity = new City(-1L, "Wakanda", "XX");
        Assertions.assertThrows(NotFoundException.class, () -> controller.getWeather(invalidCity));
    }
}
