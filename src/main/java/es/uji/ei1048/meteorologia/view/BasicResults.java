package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.App;
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

    private WeatherData wd;
    private App app;

    @Override
    public void showResults(final @NotNull City city, final @NotNull WeatherData wd) {
        this.wd = wd;
        cityRes.setText(city.getName());
        weatherRes.setText(wd.getWeather().getDescription().toUpperCase());
        humidityRes.setText(wd.getHumidity() + " RH%");
        tempRes.setText(wd.getTemperature().getCurrent() + "°C");
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
