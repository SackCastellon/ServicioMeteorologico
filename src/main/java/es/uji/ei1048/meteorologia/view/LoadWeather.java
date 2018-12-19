package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.SaveFile;
import es.uji.ei1048.meteorologia.model.converter.CityStringConverter;
import es.uji.ei1048.meteorologia.service.IWeatherProvider;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class LoadWeather {

    private final @NotNull ObjectProperty<@NotNull IWeatherProvider> provider = new SimpleObjectProperty<>();
    @FXML
    private ListView<SaveFile> saveList;
    @FXML
    private TextField searchBox;

    @FXML
    private Label error;

    @FXML
    private void initialize() {
        final @NotNull AutoCompletionBinding<@NotNull City> completionBinding = TextFields.bindAutoCompletion(searchBox, this::getSuggestions, CityStringConverter.getInstance());
        completionBinding.minWidthProperty().bind(searchBox.minWidthProperty());
        completionBinding.setOnAutoCompleted(event -> loadCity(event.getCompletion()));
        completionBinding.setDelay(0L);

        provider.setValue(OpenWeather.getInstance());
    }

    private @NotNull List<@NotNull City> getSuggestions(final @NotNull AutoCompletionBinding.ISuggestionRequest suggestionRequest) {
        return provider.get().getSuggestedCities(suggestionRequest.getUserText());
    }

    public void setDialogStage(final Stage dialogStage) {

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
