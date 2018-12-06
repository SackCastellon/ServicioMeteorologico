package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TestSaveWeatherAdvancedResultsMock {

    @Mock
    private WeatherManager api;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getSaveWeather_validSave_suc() throws Exception {
        final @NotNull WeatherData wd = new WeatherData( new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS), new Wind(20.0, 10.0),
                10.0, 10.0);
        when(api.save(any(WeatherData.class))).thenReturn(any(Boolean.class));
        Assertions.assertDoesNotThrow(() -> api.save(wd));
    }

    @Test
    void getSaveWeather_notValidSave_err() throws Exception {
        final @NotNull WeatherData wd = new WeatherData( new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS), new Wind(20.0, 10.0),
                10.0, 10.0);
        when(api.save(any(WeatherData.class))).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> api.save(wd));
    }
}
