package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.service.NotFoundException;
import es.uji.ei1048.meteorologia.view.LoadWeather;
import es.uji.ei1048.meteorologia.view.SearchPane;
import kotlin.collections.CollectionsKt;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

final class TestLoadWeatherMock {

    @Mock
    private LoadWeather controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getLoadWeather_validCity_suc() {
        when(controller.getSavedCities(anyString())).thenReturn(anyList());
        final @NotNull String query = "Madrid (ES)";
        assertDoesNotThrow(() -> controller.getSavedCities(query));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        when(controller.getSavedCities(anyString())).thenThrow(IllegalArgumentException.class);
        final @NotNull String query = "Wakanda";
        assertThrows(IllegalArgumentException.class, () -> controller.getSavedCities(query));
    }

}
