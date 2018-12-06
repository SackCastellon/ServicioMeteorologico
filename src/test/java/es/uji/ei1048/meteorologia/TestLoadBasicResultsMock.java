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

public class TestLoadBasicResultsMock {

    @Mock
    private WeatherManager api;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getLoadWeather_validCity_suc() throws Exception {
        final @NotNull String file = "Madrid.txt";
        when(api.load(anyString())).thenReturn(any(SaveFile.class));
        Assertions.assertDoesNotThrow(() -> api.load(file));
    }

    @Test
    void getCurrentWeather_notValidCity_err() throws Exception {
        final @NotNull String file = "Wakanda.txt";
        when(api.load(any(String.class))).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> api.load(file));
    }

}
