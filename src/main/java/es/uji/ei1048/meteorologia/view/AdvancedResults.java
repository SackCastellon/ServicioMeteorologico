package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
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
        rhRes.setText(wd.getHumidity() + " RH%");
        tempRes.setText(wd.getTemperature().toString());
        weatherRes.setText(wd.getWeather().toString());
        coordsRes.setText(city.getCoordinates().toString());
        windRes.setText(wd.getWind().toString());
        pressureRes.setText(wd.getPressure() + "KM/h");

    }

    public void save() {
        System.out.println("Holi");
    }
}
