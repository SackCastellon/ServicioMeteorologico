package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

public class AdvancedResults implements ISearchResults {

    @FXML
    private Label cityRes;

    @FXML
    private Label rhRes;

    @FXML
    private Label tempRes;

    @FXML
    private Label weatherRes;

    @FXML
    private Label coordsRes;

    @FXML
    private Label windRes;

    @FXML
    private Label pressureRes;

    @Override
    public void showResults(final @NotNull City city, final @NotNull WeatherData wd) {
        cityRes.setText(city.getName());
        weatherRes.setText(wd.getWeather().getDescription().toUpperCase());
        rhRes.setText(wd.getHumidity() + " RH%");
        Temperature temp = wd.getTemperature();
        tempRes.setText("Current: " + temp.getCurrent() + "°C " + "Max: " + temp.getMax() + "°C " + "Min: " + temp.getCurrent() + "°C");
        Coordinates coords = city.getCoordinates();
        coordsRes.setText("Latitude: " + coords.getLatitude() + " Longitude: " + coords.getLongitude());
        Wind wind = wd.getWind();
        windRes.setText(" " + wind.getSpeed() + " KM/h Degrees: " + wind.getDegrees());
        pressureRes.setText(wd.getPressure() + "atm");

    }

    public void save() {
        //api.save()
    }
}
