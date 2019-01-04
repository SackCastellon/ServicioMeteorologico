package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.ResultMode;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.model.WeatherManager;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class LoadWeather {

    private final @NotNull ReadOnlyListWrapper<@NotNull WeatherData> weatherData = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    @FXML
    private ListView<String> saveList;
    private WeatherManager wm;
    @FXML
    private ChoiceBox cbCity;
    private static final @NotNull String ENUM_PROPERTY = "enumProperty";
    @FXML
    private Label error;
    private final @NotNull ReadOnlyObjectWrapper<@NotNull ResultMode> resultMode = new ReadOnlyObjectWrapper<>(ResultMode.BASIC);
    @FXML
    private RadioButton displayBasic;
    @FXML
    private RadioButton displayAdvanced;
    private List<WeatherData> data;

    @FXML
    private void initialize() throws IOException {
        displayBasic.getProperties().put(ENUM_PROPERTY, ResultMode.BASIC);
        displayAdvanced.getProperties().put(ENUM_PROPERTY, ResultMode.ADVANCED);
        wm = WeatherManager.getInstance();
        @NotNull List<String> cities = wm.getSavedCities();
        if(cities.isEmpty()) throw new IOException();
        for (String svc : cities
        ) {
            cbCity.getItems().add(svc);
        }
        cbCity.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldInd, newInd) -> loadSavedCities((String) cbCity.getItems().get((Integer) newInd)));
    }

    private void loadSave(){

    }


    private void loadSavedCities(final @NotNull String query) {
        data = wm.load(query);
        saveList.getItems().clear();
        for (WeatherData wd : data
        ) {
            saveList.getItems().add(wd.getDateTime().toString());
        }
    }


}
