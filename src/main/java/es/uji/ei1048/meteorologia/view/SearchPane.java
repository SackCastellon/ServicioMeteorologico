package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.App;
import es.uji.ei1048.meteorologia.api.CityNotFoundException;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.service.IWeatherService;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static es.uji.ei1048.meteorologia.view.SearchPane.DisplayMode.ADVANCED;
import static es.uji.ei1048.meteorologia.view.SearchPane.WeatherMode.FORECAST;

public final class SearchPane {

    private static final @NotNull Pattern ONE_TO_FIVE = Pattern.compile("[1-5]");
    private static final @NotNull Pattern NOT_ONE_TO_FIVE = Pattern.compile("^[1-5]");
    private static final @NotNull String ENUM_PROPERTY = "enumProperty"; //NON-NLS

    private final @NotNull ObjectProperty<@NotNull DisplayMode> displayMode = new SimpleObjectProperty<>(DisplayMode.BASIC);
    private final @NotNull ObjectProperty<@NotNull WeatherMode> weatherMode = new SimpleObjectProperty<>(WeatherMode.CURRENT);

    @FXML
    private ResourceBundle resources; // TODO Use resource bundle for translations
    @FXML
    private TextField searchBar;
    @FXML
    private RadioButton displayBasic;
    @FXML
    private ToggleGroup displayGroup;
    @FXML
    private RadioButton displayAdvanced;
    @FXML
    private RadioButton weatherCurrent;
    @FXML
    private ToggleGroup weatherGroup;
    @FXML
    private RadioButton weatherForecast;
    @FXML
    private Label daysLabel;
    @FXML
    private TextField days;
    @FXML
    private Label rangeLabel;
    @FXML
    private TextField rangeDays;
    @FXML
    private Label error;
    @FXML
    private Button csButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;

    private App app;
    private IWeatherService service;
    private List<WeatherData> current;

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> void bindToggleGroup(
            final @NotNull Property<@NotNull T> property,
            final @NotNull ToggleGroup toggleGroup
    ) {
        property.bind(Bindings.createObjectBinding(
                () -> (T) Objects.requireNonNull(toggleGroup).getSelectedToggle().getProperties().get(ENUM_PROPERTY),
                toggleGroup.selectedToggleProperty()
        ));
    }

    @FXML
    private void initialize() {
        service = new OpenWeather();
        error.setText("");
        days.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!ONE_TO_FIVE.matcher(newValue).matches()) {
                days.setText(NOT_ONE_TO_FIVE.matcher(newValue).replaceAll(""));
            }
        });

        displayBasic.getProperties().put(ENUM_PROPERTY, DisplayMode.BASIC);
        displayAdvanced.getProperties().put(ENUM_PROPERTY, ADVANCED);
        bindToggleGroup(displayMode, displayGroup);

        weatherCurrent.getProperties().put(ENUM_PROPERTY, WeatherMode.CURRENT);
        weatherForecast.getProperties().put(ENUM_PROPERTY, FORECAST);
        bindToggleGroup(weatherMode, weatherGroup);

        final @NotNull BooleanBinding isNotForecast = weatherMode.isNotEqualTo(FORECAST);
        daysLabel.disableProperty().bind(isNotForecast);
        days.disableProperty().bind(isNotForecast);
        rangeLabel.disableProperty().bind(isNotForecast);
        rangeDays.disableProperty().bind(isNotForecast);
    }

    public void setService(final IWeatherService service) {
        this.service = service;
    }

    public void setApp(final App app) {
        this.app = app;
    }

    public void saveAll() {
        app.saveAll(current);
    }

    @FXML
    private void search() {
        /*final String query = searchBar.getText();
        if (query == null || query.isEmpty()) {
            error.setText("Input a valid city");
        } else {
            if (error.getText() != null && !error.getText().isEmpty()) {
                error.setText("");
            }
            try {
                error.setText("");
                final @NotNull City city = new City(-1, query, "", new Coordinates(-1.0, -1.0)); // FIXME
                if (weatherMode.get() == FORECAST) {
                    final int n_days = Integer.parseInt(days.getText());
                    final List<WeatherData> wdList = service.getForecast(Objects.requireNonNull(city), n_days);
                    current = wdList;
                    app.showForecastSearchResult(wdList, displayMode.get() == ADVANCED);
                    saveButton.setDisable(false);
                } else {
                    final WeatherData wd = service.getWeather(Objects.requireNonNull(city));
                    app.showSearchResult(wd, displayMode.get() == ADVANCED);
                    if (!saveButton.isDisabled()) {
                        saveButton.setDisable(true);
                    }
                }
            } catch (final NotFoundException e) {
                error.setText("Ciudad no encontrada");
            }
        }*/
    }

    @FXML
    private void showLoadScreen() {
        app.showLoadScreen();
    }

    public @NotNull WeatherData getWeather(final @NotNull String query) {
        if (query.isEmpty()) throw new IllegalArgumentException("The query text must not be empty!");
        final int cityId = service.getCityId(query).orElseThrow(() -> new CityNotFoundException(query));
        return service.getWeather(cityId);
    }

    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull String query, final int offset) {
        if (query.isEmpty()) throw new IllegalArgumentException("The query text must not be empty!");
        final int cityId = service.getCityId(query).orElseThrow(() -> new CityNotFoundException(query));
        return service.getForecast(cityId, offset);
    }

    enum DisplayMode {BASIC, ADVANCED}

    enum WeatherMode {CURRENT, FORECAST}
}
