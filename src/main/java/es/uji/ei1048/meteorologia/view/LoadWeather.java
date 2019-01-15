package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.NoSavedCitiesException;
import es.uji.ei1048.meteorologia.model.ResultMode;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.model.WeatherManager;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static es.uji.ei1048.meteorologia.util.Utils.bindToToggleGroup;

public class LoadWeather {

    private static final @NotNull String ENUM_PROPERTY = "enumProperty"; //NON-NLS
    private static final @NotNull DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"); //NON-NLS

    private final @NotNull ListProperty<@NotNull WeatherData> weatherData = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final @NotNull ObjectProperty<@NotNull ResultMode> resultMode = new SimpleObjectProperty<>(ResultMode.BASIC);

    private final @NotNull WeatherManager weatherManager = WeatherManager.getInstance();

    @FXML
    private ResourceBundle resources;
    @FXML
    private ListView<WeatherData> saveList;
    @FXML
    private ChoiceBox<String> cbCity;
    @FXML
    private ToggleGroup groupDisplay;
    @FXML
    private RadioButton displayBasic;
    @FXML
    private RadioButton displayAdvanced;
    @FXML
    private Button buttonLoad;

    private Stage stage;
    private boolean success = false;

    @FXML
    private void initialize() {
        displayBasic.getProperties().put(ENUM_PROPERTY, ResultMode.BASIC);
        displayAdvanced.getProperties().put(ENUM_PROPERTY, ResultMode.ADVANCED);
        bindToToggleGroup(resultMode, groupDisplay, ENUM_PROPERTY);

        final @NotNull List<@NotNull String> savedCities = weatherManager.getSavedCities();
        if (savedCities.isEmpty()) throw new NoSavedCitiesException();
        cbCity.getItems().setAll(savedCities);
        cbCity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadSavedCities(newValue));

        weatherData.bindContent(saveList.getSelectionModel().getSelectedItems());
        saveList.setCellFactory(param -> new WeatherDataListCell());

        buttonLoad.disableProperty().bind(saveList.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void load() {
        success = true;
        stage.close();
    }

    private void loadSavedCities(final @NotNull String query) {
        saveList.getItems().setAll(getSavedCities(query));
    }

    public List<WeatherData> getSavedCities(final @NotNull String query){
        return weatherManager.load(query);
    }

    public void setStage(final @NotNull Stage stage) {
        this.stage = stage;
    }

    public boolean isSuccess() {
        return success;
    }

    public @NotNull List<@NotNull WeatherData> getWeatherData() {
        return Collections.unmodifiableList(weatherData);
    }

    public @NotNull ResultMode getResultMode() {
        return resultMode.get();
    }

    private static final class WeatherDataListCell extends ListCell<WeatherData> {
        @Override
        protected void updateItem(final WeatherData item, final boolean empty) {
            super.updateItem(item, empty);
            if (empty) setText("");
            else setText(TIME_FORMATTER.format(item.getDateTime()));
        }
    }
}
