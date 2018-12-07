package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

final class TestSaveWeatherBasicResultsMock {

    @Mock
    private WeatherManager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getSaveWeather_validSave_suc() {
        final @NotNull WeatherData wd = new WeatherData(
                LocalDateTime.now(),
                new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0)
        );

        when(manager.save(any(WeatherData.class))).thenReturn(true);
        Assertions.assertTrue(manager.save(wd));
    }

    @Test
    void getSaveWeather_notValidSave_err() {
        final @NotNull WeatherData wd = new WeatherData(
                LocalDateTime.now(),
                new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0)
        );

        when(manager.save(any(WeatherData.class))).thenReturn(false);
        Assertions.assertFalse(manager.save(wd));
    }

}
