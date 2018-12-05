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

public final class SearchPane {

    @FXML
    ToggleGroup searchMode;
    String ns = "No city searched yet";
    private App app;
    private IWeatherApi api;
    private boolean mode;
    @FXML
    private TextField searchBar;
    @FXML
    private Label error;

    @FXML
    private void initialize() {
        api = new OpenWeatherApi();
        error.setText("");
        searchMode.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            if (oldToggle != newToggle) {
                mode = !mode;
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
                final WeatherData wd = api.getWeather(val);
                error.setText("");

                app.showSearchResults(val, wd, mode);

            } catch (final NotFoundException e) {
                error.setText("Ciudad no encontrada");
            }
        }
    }

    enum Mode {BASIC, ADVANCED}
}
