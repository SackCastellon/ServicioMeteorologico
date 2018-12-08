package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

final class TestForecastWeatherMock {

    @Mock
    private IWeatherService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getForecastWeather_validCity_suc() {
        when(service.getForecast(anyInt(), anyInt())).thenReturn(anyList());

        final SearchPane controller = new SearchPane();
        controller.setService(service);

        Assertions.assertNotNull(controller.getForecast("Madrid", 3));
    }

    @Test
    void geForecastWeather_notValidCity_err() {
        when(service.getForecast(anyInt(), anyInt())).thenThrow(NotFoundException.class);

        final SearchPane controller = new SearchPane();
        controller.setService(service);

        Assertions.assertThrows(NotFoundException.class, () -> controller.getForecast("Wakanda", 3));
    }
}
