package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.WeatherManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestLoadBasicResults {

    private static WeatherManager api;

    @BeforeAll
    static void setUp() {
        api = new WeatherManager();
    }

    @Test
    void getLoadWeather_validCity_suc() {
        final @NotNull String file = "Madrid.txt";
        Assertions.assertDoesNotThrow(() -> api.load(file));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        final @NotNull String file = "Wakanda.txt";
        Assertions.assertThrows(NotFoundException.class, () -> api.load(file));
    }

}
