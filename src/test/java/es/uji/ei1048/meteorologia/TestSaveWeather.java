package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.*;
import es.uji.ei1048.meteorologia.view.ResultsPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Comparator;

final class TestSaveWeather {

    private static WeatherManager manager;
    private static ResultsPane controller;

    @BeforeAll
    static void setUp() {
        manager = WeatherManager.getInstance();
        controller = new ResultsPane();
        controller.managerProperty().set(manager);
    }

    @BeforeEach
    void deleteSaves() {
        Path path = WeatherManager.getDataDir();
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void getSaveWeather_validSave_suc() {
        final @NotNull City city = new City(6359304L, "Zaragoza", "ES");
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
        final @NotNull City city = new City(6359304L, "Madrid", "ES");
        for (int i = 0; i < 7; i++) {
            controller.save(new WeatherData(
                    city,
                    LocalDateTime.now(),
                    new Weather(10 + i, "Viento", "mucho frio"),
                    new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                    new Wind(20.0, 10.0),
                    10.0,
                    10.0));
        }
        Assertions.assertThrows(ExceptionInInitializerError.class, () -> controller.save(new WeatherData(
                city,
                LocalDateTime.now(),
                new Weather(1, "Viento", "mucho frio"),
                new Temperature(15.0, 10.0, 20.0, Temperature.Units.CELSIUS),
                new Wind(20.0, 10.0),
                10.0,
                10.0)));
    }

}
