package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.api.IWeatherApi;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.api.OpenWeatherApi;
import es.uji.ei1048.meteorologia.model.WeatherData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SearchPane {

        IWeatherApi api = null;

        @FXML
        private TextField searchBar;

        @FXML
        private Label error;

        String ns = "No city searched yet";

        @FXML
        private void initialize() {
            api = new OpenWeatherApi();
            error.setText("");
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

                } catch (NotFoundException e) {
                    error.setText("Ciudad no encontrada");
                }
            }
        }
    }