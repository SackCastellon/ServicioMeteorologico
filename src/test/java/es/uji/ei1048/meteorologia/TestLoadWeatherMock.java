package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.SaveFile;
import es.uji.ei1048.meteorologia.model.WeatherManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

final class TestLoadWeatherMock {

    @Mock
    private WeatherManager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getLoadWeather_validCity_suc() {
        final @NotNull String file = "Madrid.txt";
        when(manager.load(anyString())).thenReturn(any(SaveFile.class));
        Assertions.assertDoesNotThrow(() -> manager.load(file));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        final @NotNull String file = "Wakanda.txt";
        when(manager.load(anyString())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> manager.load(file));
    }

}
