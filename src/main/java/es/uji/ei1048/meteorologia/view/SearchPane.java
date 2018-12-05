package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.App;
import es.uji.ei1048.meteorologia.api.IWeatherApi;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.api.OpenWeatherApi;
import es.uji.ei1048.meteorologia.model.WeatherData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;


public class SearchPane {

    private App app;

    private IWeatherApi api = null;

    @FXML
    ToggleGroup searchMode;

    private boolean mode;

    @FXML
    private TextField searchBar;

    @FXML
    private Label error;

    String ns = "No city searched yet";

    @FXML
    private void initialize() {
        api = new OpenWeatherApi();
        error.setText("");
        searchMode.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (old_toggle != new_toggle) {
                mode = !mode;
            }
        });
    }

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private void search() {
        String val = searchBar.getText();
        if (val.equals("")) {
            error.setText("Input a valid city");
        } else {
            //api.search(val);
            if (!error.getText().equals("")) {
                error.setText("");
            }
            try {
                WeatherData wd = api.getWeather(val);
                error.setText("");

                app.showSearchResults(val, wd, mode);

            } catch (NotFoundException e) {
                error.setText("Ciudad no encontrada");
            }
        }
    }
}