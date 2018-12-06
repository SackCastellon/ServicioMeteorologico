package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.App;
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
    private App app;
    private WeatherData wd;

    @Override
    public void showResults(final @NotNull City city, final @NotNull WeatherData wd) {
        this.wd = wd;
        cityRes.setText(city.getName());
        weatherRes.setText(wd.getWeather().getDescription().toUpperCase());
        rhRes.setText(wd.getHumidity() + " RH%");
        final Temperature temp = wd.getTemperature();
        tempRes.setText("Current: " + temp.getCurrent() + "°C " + "Max: " + temp.getMax() + "°C " + "Min: " + temp.getCurrent() + "°C");
        final Coordinates coords = city.getCoordinates();
        coordsRes.setText("Latitude: " + coords.getLatitude() + " Longitude: " + coords.getLongitude());
        final Wind wind = wd.getWind();
        windRes.setText(" " + wind.getSpeed() + " KM/h Degrees: " + wind.getDegrees());
        pressureRes.setText(wd.getPressure() + "atm");

    }

    @Override
    public void save() {
        app.save(wd);
    }

    @Override
    public void setApp(final App app) {
        this.app = app;
    }
}
