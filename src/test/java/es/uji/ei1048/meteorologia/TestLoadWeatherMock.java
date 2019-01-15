package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.service.NotFoundException;
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
    private WeatherManager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getLoadWeather_validCity_suc() {
        CollectionsKt.listOf();

        when(manager.load(anyString())).thenReturn(anyList());

        final @NotNull SearchPane controller = new SearchPane();
        controller.managerProperty().set(manager);

        final @NotNull String file = "Madrid (ES)";
        assertDoesNotThrow(() -> manager.load(file));
    }

    @Test
    void getCurrentWeather_notValidCity_err() {
        when(manager.load(anyString())).thenThrow(NotFoundException.class);

        final @NotNull SearchPane controller = new SearchPane();
        controller.managerProperty().set(manager);

        final @NotNull String file = "Wakanda";
        assertThrows(NotFoundException.class, () -> manager.load(file));
    }

}
