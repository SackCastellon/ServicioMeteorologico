package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.App;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.Coordinates;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class SearchPane {

    @FXML
    ToggleGroup searchMode;
    @FXML
    ToggleGroup dateMode;
    private App app;
    private IWeatherService api;
    private boolean advanced;
    @FXML
    private TextField searchBar;
    @FXML
    private TextField days;
    @FXML
    private Label error;
    private boolean forecast;
    @FXML
    private Button saveButton;

    private List<WeatherData> current;

    @FXML
    private Label daysLabel;


    @FXML
    private void initialize() {
        api = new OpenWeather();
        error.setText("");
        days.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                days.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        searchMode.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            if (oldToggle != newToggle) {
                advanced = !advanced;
            }
        });
        dateMode.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            if (oldToggle != newToggle) {
                forecast = !forecast;
                daysLabel.setDisable(!forecast);
                days.setDisable(!forecast);
            }
        });
    }

    public void setApi(final IWeatherService api) {
        this.api = api;
    }

    public void setApp(final App app) {
        this.app = app;
    }

    public void saveAll() {
        app.saveAll(current);
    }

    @FXML
    private void search() {
        final String query = searchBar.getText();
        if (query == null || query.isEmpty()) {
            error.setText("Input a valid city");
        } else {
            if (error.getText() != null && !error.getText().isEmpty()) {
                error.setText("");
            }
            try {
                error.setText("");
                final @NotNull City city = new City(-1, query, "", new Coordinates(-1.0, -1.0)); // FIXME
                if (forecast) {
                    int n_days = Integer.parseInt(days.getText());
                    final List<WeatherData> wdList = api.getForecast(Objects.requireNonNull(city), n_days);
                    current = wdList;
                    app.showForecastSearchResult(wdList, advanced);
                    saveButton.setDisable(false);
                } else {
                    final WeatherData wd = api.getWeather(Objects.requireNonNull(city));
                    app.showSearchResult(wd, advanced);
                    if (!saveButton.isDisabled()) {
                        saveButton.setDisable(true);
                    }
                }
            } catch (final NotFoundException e) {
                error.setText("Ciudad no encontrada");
            }
        }
    }

    @FXML
    private void showLoadScreen() {
        app.showLoadScreen();
    }

    enum Mode {BASIC, ADVANCED}
}
