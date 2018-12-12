package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import es.uji.ei1048.meteorologia.view.SearchPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class TestForecastWeather {

    @Test
    void getForecastWeather_validCity_suc() {
        final SearchPane controller = new SearchPane();
        controller.providerProperty().set(new OpenWeather());

        final City validCity = new City(6359304L, "Madrid", "ES");
        Assertions.assertNotNull(controller.getForecast(validCity, 3, 1));
    }

    @Test
    void getForecastWeather_notValidCity_err() {
        final SearchPane controller = new SearchPane();
        controller.providerProperty().set(new OpenWeather());

        final City invalidCity = new City(-1L, "Wakanda", "XX");
        Assertions.assertThrows(NotFoundException.class, () -> controller.getForecast(invalidCity, 3, 1));
    }
}
