package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class TestForecastWeather {

    @Test
    void getForecastWeather_validCity_suc() {
        final SearchPane controller = new SearchPane();
        controller.setService(new OpenWeather());

        Assertions.assertNotNull(controller.getForecast("Madrid", 3));
    }

    @Test
    void getForecastWeather_notValidCity_err() {
        final SearchPane controller = new SearchPane();
        controller.setService(new OpenWeather());

        Assertions.assertThrows(NotFoundException.class, () -> controller.getForecast("Wakanda", 3));
    }
}
