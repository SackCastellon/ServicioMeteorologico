package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.service.NotFoundException;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyList;
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
        when(manager.load(anyString())).thenReturn(anyList());

        final @NotNull SearchPane controller = new SearchPane();
        controller.managerProperty().set(manager);

        final @NotNull String file = "Madrid.txt";
        Assertions.assertDoesNotThrow(() -> manager.load(file));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        when(manager.load(anyString())).thenThrow(NotFoundException.class);

        final @NotNull SearchPane controller = new SearchPane();
        controller.managerProperty().set(manager);

        final @NotNull String file = "Wakanda.txt";
        Assertions.assertThrows(NotFoundException.class, () -> manager.load(file));
    }

}
