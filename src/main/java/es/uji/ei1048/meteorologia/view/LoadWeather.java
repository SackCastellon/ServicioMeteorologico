package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.SaveFile;
import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.service.IWeatherProvider;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LoadWeather {

    private final @NotNull ObjectProperty<@NotNull IWeatherProvider> provider = new SimpleObjectProperty<>();
    @FXML
    private ListView<SaveFile> saveList;
    WeatherManager wm;
    @FXML
    private ChoiceBox cb_city;

    @FXML
    private Label error;

    @FXML
    private void initialize() {
        wm = WeatherManager.getInstance();
        for (String svc : wm.getSavedCities()
        ) {
            cb_city.getItems().add(svc);
        }
    }



    private void loadCity(final @NotNull String query) {
        if (query.isEmpty()) {
            error.setText("Search field must not be empty.");
        } else {
            final @NotNull Optional<City> city = provider.get().getCity(query);
            if (city.isPresent()) {
                error.setText("");
                loadCity(city.get());
            } else {
                error.setText("City not found.");
            }
        }
    }

    private void loadCity(final @NotNull City city) {
        System.out.println("Cargados elementos de la ciudad: " + city.getName());
    }

    public SaveFile getSel() {
        return saveList.getSelectionModel().getSelectedItem();
    }
}
