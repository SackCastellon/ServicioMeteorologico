package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.*;
import es.uji.ei1048.meteorologia.view.ResultsPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

final class TestSaveWeatherMock {

    @Mock
    private WeatherManager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getSaveWeather_validSave_suc() {
        when(manager.save(any(WeatherData.class))).thenReturn(true);

        final @NotNull ResultsPane controller = new ResultsPane();
        controller.managerProperty().set(manager);

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
        Assertions.assertTrue(manager.save(wd));
    }

    @Test
    void getSaveWeather_notValidSave_err() {
        when(manager.save(any(WeatherData.class))).thenReturn(false);

        final @NotNull ResultsPane controller = new ResultsPane();
        controller.managerProperty().set(manager);

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
        Assertions.assertFalse(manager.save(wd));
    }
}
