package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.IWeatherAPI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class TestGetCurrentWeather {


    IWeatherAPI api = null;

    @FXML
    private TextField searchBar;

    @FXML
    private Label city;

    @FXML
    private Label temp;

    @FXML
    private Label rh;

    @FXML
    private Label status;

    @FXML
    private Label error;

    String ns = "No city searched yet";

    @FXML
    private void initialize() {
        //api = new OpenWeatherApi();
        error.setText("");
        city.setText(ns);
        temp.setText(ns);
        rh.setText(ns);
        status.setText(ns);
    }

    @FXML
    private void searchCurrent() {
        String val = searchBar.getText();
        if (val.equals("")){
            error.setText("Input a valid city");
        } else {
            //api.search(val);
            if ( !error.getText().equals("")){
                error.setText("");
            }
            System.out.println("Holi");
        }
    }

}
