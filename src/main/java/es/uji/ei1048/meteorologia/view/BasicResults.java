package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.WeatherData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BasicResults implements ISearchResults {

    @FXML
    private Label cityRes;

    @FXML
    private Label rhRes;

    @FXML
    private Label tempRes;

    @FXML
    private Label weatherRes;


    @Override
    public void showResults(String city, WeatherData wd) {
        cityRes.setText(city);
        rhRes.setText(wd.getHumidity()+"RH%");
        tempRes.setText(wd.getTemperature().toString());
        weatherRes.setText(wd.getWeather().get(0).toString());
    }
}
