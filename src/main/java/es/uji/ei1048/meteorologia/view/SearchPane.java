package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.App;
import es.uji.ei1048.meteorologia.api.IWeatherApi;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.api.OpenWeatherApi;
import es.uji.ei1048.meteorologia.model.WeatherData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.util.List;
import java.util.Objects;

public final class SearchPane {

    @FXML
    ToggleGroup searchMode;
    String ns = "No city searched yet";
    private App app;
    private IWeatherApi api;
    @FXML
    ToggleGroup dateMode;
    private boolean advanced;
    @FXML
    private TextField searchBar;
    @FXML
    private Label error;
    private boolean forecast;

    @FXML
    private void initialize() {
        api = new OpenWeatherApi();
        error.setText("");
        searchMode.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            if (oldToggle != newToggle) {
                advanced = !advanced;
            }
        });
        dateMode.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            if (oldToggle != newToggle) {
                forecast = !forecast;
            }
        });
    }

    public void setApp(final App app) {
        this.app = app;
    }

    @FXML
    private void search() {
        final String val = searchBar.getText();
        if (val != null && val.isEmpty()) {
            error.setText("Input a valid city");
        } else {
            //api.search(val);
            if (error.getText() != null && !error.getText().isEmpty()) {
                error.setText("");
            }
            try {
                error.setText("");
                if (forecast) {
                    final WeatherData wd = api.getWeather(Objects.requireNonNull(val));
                    app.showSearchResults(val, wd, advanced);
                } else {
                    final List<WeatherData> wdList = api.getForecast(Objects.requireNonNull(val), 3);
                    app.showForecastSearchResult(val, wdList, advanced);
                }
            } catch (final NotFoundException e) {
                error.setText("Ciudad no encontrada");
            }
        }
    }

    enum Mode {BASIC, ADVANCED}
}
