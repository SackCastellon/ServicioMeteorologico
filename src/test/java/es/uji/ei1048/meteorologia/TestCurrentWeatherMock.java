package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.view.SearchPane;
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
    private IWeatherService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getCurrentWeather_validCity_suc() {
        when(service.getWeather(anyString())).thenReturn(any(WeatherData.class));

        final SearchPane controller = new SearchPane();
        controller.setService(service);

        Assertions.assertNotNull(controller.getWeather("Madrid"));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        when(service.getWeather(anyString())).thenThrow(NotFoundException.class);

        final SearchPane controller = new SearchPane();
        controller.setService(service);

        Assertions.assertThrows(NotFoundException.class, () -> controller.getWeather("Wakanda"));
    }
}
