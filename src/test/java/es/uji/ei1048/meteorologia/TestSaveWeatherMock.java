package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.*;
import es.uji.ei1048.meteorologia.view.ResultsPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

final class TestSaveWeatherMock {


    @Mock
    private ResultsPane controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getSaveWeather_validSave_suc() {
        when(controller.save(any(WeatherData.class))).thenReturn(true);
        final @NotNull City city = new City(6359304L, "Madrid", "ES");
        final @NotNull WeatherData wd = new WeatherData(
                city,
                LocalDateTime.now(),
                new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0),
                10.0,
                10.0
        );
        assertTrue(controller.save(wd));
    }

    @Test
    void getSaveWeather_notValidSave_err() {
        when(controller.save(any(WeatherData.class))).thenReturn(false);
        final @NotNull City city = new City(6359304L, "Madrid", "ES");
        final @NotNull WeatherData wd = new WeatherData(
                city,
                LocalDateTime.now(),
                new Weather(10, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0),
                10.0,
                10.0
        );
        assertFalse(controller.save(wd));
    }
}
