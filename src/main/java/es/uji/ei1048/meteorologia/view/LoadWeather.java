package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.SaveFile;
import es.uji.ei1048.meteorologia.model.converter.CityStringConverter;
import es.uji.ei1048.meteorologia.service.IWeatherProvider;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LoadWeather {

    @FXML
    private ListView<SaveFile> saveList;

    private final @NotNull ObjectProperty<@NotNull IWeatherProvider> provider = new SimpleObjectProperty<>();
    @FXML
    private TextField searchBox;

    @FXML
    private void initialize() {
        final @NotNull AutoCompletionBinding<@NotNull City> completionBinding = TextFields.bindAutoCompletion(searchBox, this::getSuggestions, CityStringConverter.getInstance());
        completionBinding.minWidthProperty().bind(searchBox.minWidthProperty());
        completionBinding.setOnAutoCompleted(event -> searchSaves(event.getCompletion()));
        completionBinding.setDelay(0L);
    }

    private @NotNull List<@NotNull City> getSuggestions(final @NotNull AutoCompletionBinding.ISuggestionRequest suggestionRequest) {
        return provider.get().getSuggestedCities(suggestionRequest.getUserText());
    }

    public void setDialogStage(final Stage dialogStage) {

    }

    private void searchSaves(final @NotNull City city) {

    }

    public SaveFile getSel() {
        return saveList.getSelectionModel().getSelectedItem();
    }
}
