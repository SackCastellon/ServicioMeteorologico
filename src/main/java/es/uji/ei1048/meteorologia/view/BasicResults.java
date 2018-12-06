package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

public final class BasicResults implements ISearchResults {

    @FXML
    private Label cityRes;

    @FXML
    private Label humidityRes;

    @FXML
    private Label tempRes;

    @FXML
    private Label weatherRes;

    @Override
    public void showResults(final @NotNull City city, final @NotNull WeatherData wd) {
        cityRes.setText(city.getName());
        weatherRes.setText(wd.getWeather().getDescription().toUpperCase());
        humidityRes.setText(wd.getHumidity() + " RH%");
        tempRes.setText(wd.getTemperature().getCurrent() + "°C");
    }


}
