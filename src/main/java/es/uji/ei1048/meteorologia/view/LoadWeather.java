package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.model.WeatherManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LoadWeather {


    @FXML
    private ListView<String> saveList;
    WeatherManager wm;
    @FXML
    private ChoiceBox cbCity;

    @FXML
    private Label error;

    @FXML
    private void initialize() {
        wm = WeatherManager.getInstance();
        for (String svc : wm.getSavedCities()
        ) {
            cbCity.getItems().add(svc);
        }
        cbCity.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldInd, newInd) -> loadCity((String) cbCity.getItems().get((Integer) newInd)));
    }



    private void loadCity(final @NotNull String query) {
        List<WeatherData> data = wm.load(query);
        saveList.getItems().clear();
        for (WeatherData wd : data
        ) {
            saveList.getItems().add("Dia: " + wd.getDateTime().toString());
        }
    }


    private void loadCity(final @NotNull City city) {
        System.out.println("Cargados elementos de la ciudad: " + city.getName());
    }

}
