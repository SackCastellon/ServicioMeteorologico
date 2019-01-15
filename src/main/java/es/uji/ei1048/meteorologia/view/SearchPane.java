package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.App;
import es.uji.ei1048.meteorologia.NoSavedCitiesException;
import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.ResultMode;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.model.converter.CityStringConverter;
import es.uji.ei1048.meteorologia.service.IWeatherProvider;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static es.uji.ei1048.meteorologia.util.Utils.bindToToggleGroup;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest;

public class SearchPane {

    private static final @NotNull String ENUM_PROPERTY = "enumProperty"; //NON-NLS

    private static final @NotNull ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory() {
        private final @NotNull ThreadGroup group;
        private final @NotNull AtomicInteger threadNumber = new AtomicInteger(1);
        @NonNls
        private final @NotNull String namePrefix;

        {
            final @Nullable SecurityManager manager = System.getSecurityManager();
            group = (manager != null) ? manager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-weather-thread-"; //NON-NLS
        }

        @Override
        public @NotNull Thread newThread(final @NotNull Runnable r) {
            final @NotNull Thread thread = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    });

    private final @NotNull ObjectProperty<@NotNull IWeatherProvider> provider = new SimpleObjectProperty<>();
    private final @NotNull ObjectProperty<@NotNull WeatherManager> manager = new SimpleObjectProperty<>();
    private final @NotNull StringProperty status = new SimpleStringProperty();

    private final @NotNull ListProperty<@NotNull WeatherData> weatherData = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final @NotNull ObjectProperty<@NotNull ResultMode> resultMode = new SimpleObjectProperty<>(ResultMode.BASIC);
    private final @NotNull ObjectProperty<@NotNull WeatherMode> weatherMode = new SimpleObjectProperty<>(WeatherMode.CURRENT);

    private final @NotNull LocalDate minDate = LocalDate.now().plusDays(1L);
    private final @NotNull ObjectExpression<@NotNull LocalDate> maxForecastDayProperty =
            Bindings.createObjectBinding(() -> minDate.plusDays((long) provider.get().getMaxForecastDays() - 1L), provider);

    private App app;

    @FXML
    private ResourceBundle resources;

    @FXML
    private TextField searchBox;
    @FXML
    private ToggleGroup groupDisplay;
    @FXML
    private RadioButton displayBasic;
    @FXML
    private RadioButton displayAdvanced;
    @FXML
    private ToggleGroup groupWeather;
    @FXML
    private RadioButton weatherCurrent;
    @FXML
    private RadioButton weatherForecast;
    @FXML
    private VBox forecastOptions;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Label error;
    @FXML
    private Button loadBtn;
    @FXML
    private Button searchBtn;

    public @NotNull ObjectProperty<@NotNull IWeatherProvider> providerProperty() {
        return provider;
    }

    public @NotNull ObjectProperty<@NotNull WeatherManager> managerProperty() {
        return manager;
    }

    public @NotNull StringProperty statusProperty() {
        return status;
    }

    @FXML
    private void initialize() {
        final @NotNull AutoCompletionBinding<@NotNull City> completionBinding = TextFields.bindAutoCompletion(searchBox, this::getSuggestions, CityStringConverter.getInstance());
        completionBinding.minWidthProperty().bind(searchBox.minWidthProperty());
        completionBinding.setOnAutoCompleted(event -> search(event.getCompletion()));

        displayBasic.getProperties().put(ENUM_PROPERTY, ResultMode.BASIC);
        displayAdvanced.getProperties().put(ENUM_PROPERTY, ResultMode.ADVANCED);
        bindToToggleGroup(resultMode, groupDisplay, ENUM_PROPERTY);

        weatherCurrent.getProperties().put(ENUM_PROPERTY, WeatherMode.CURRENT);
        weatherForecast.getProperties().put(ENUM_PROPERTY, WeatherMode.FORECAST);
        bindToToggleGroup(weatherMode, groupWeather, ENUM_PROPERTY);

        final @NotNull BooleanExpression isNotForecast = weatherMode.isNotEqualTo(WeatherMode.FORECAST);
        forecastOptions.disableProperty().bind(isNotForecast);

        fromDate.setValue(minDate);
        fromDate.setDayCellFactory(picker -> new ForecastDateCell(new SimpleObjectProperty<>(minDate), toDate.valueProperty()));
        fromDate.setEditable(false);
        toDate.setValue(minDate);
        toDate.setDayCellFactory(picker -> new ForecastDateCell(fromDate.valueProperty(), maxForecastDayProperty));
        toDate.setEditable(false);

        loadBtn.setOnAction(event -> openLoadPane());
        searchBtn.setOnAction(event -> search(searchBox.getText()));

        Platform.runLater(() -> searchBox.requestFocus());
    }

    public void setApp(final @NotNull App app) {
        this.app = app;
    }

    private void openLoadPane() {
        try {
            app.showLoadWindow();
        } catch (final NoSavedCitiesException e) {
            error.setText(resources.getString("error.noSavedCities"));
        }
    }

    private void search(final @NotNull String query) {
        if (query.isEmpty()) {
            error.setText(resources.getString("error.empty"));
        } else {
            final @NotNull Optional<City> city = provider.get().getCity(query);
            if (city.isPresent()) {
                error.setText("");
                search(city.get());
            } else {
                error.setText(resources.getString("error.notFound"));
            }
        }
    }

    private void search(final @NotNull City city) {
        //status.setValue(resources.getString("status.loading"));
        switch (weatherMode.get()) {
            case CURRENT:
                executorService.execute(() -> {
                    final @NotNull WeatherData data = getWeather(city);
                    Platform.runLater(() -> {
                        weatherData.setAll(data);
                        //status.setValue(resources.getString("status.loaded"));
                    });
                });
                break;
            case FORECAST:
                final @NotNull LocalDate from = fromDate.getValue();
                final @NotNull LocalDate to = toDate.getValue();
                final int offset = (int) DAYS.between(minDate, from) + 1;
                final int count = (int) DAYS.between(from, to) + 1;

                executorService.execute(() -> {
                    final @NotNull List<@NotNull WeatherData> data = getForecast(city, offset, count);
                    Platform.runLater(() -> {
                        weatherData.setAll(data);
                        //status.setValue(resources.getString("status.loaded"));
                    });
                });
                break;
        }
    }

    public @NotNull WeatherData getWeather(final @NotNull City city) {
        return provider.get().getWeather(city);
    }

    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull City city, final int offset, final int count) {
        return provider.get().getForecast(city, offset, count);
    }

    private @NotNull List<@NotNull City> getSuggestions(final @NotNull ISuggestionRequest suggestionRequest) {
        return provider.get().getSuggestedCities(suggestionRequest.getUserText());
    }


    public void bindWeatherData(final @NotNull ObservableList<@NotNull WeatherData> list) {
        weatherData.bindContentBidirectional(list);
    }

    public void bindResultMode(final @NotNull Property<@NotNull ResultMode> property) {
        resultMode.bindBidirectional(property);
    }


    private enum WeatherMode {CURRENT, FORECAST}

    private static final class ForecastDateCell extends DateCell {
        private final @NotNull ObservableValue<@NotNull LocalDate> min;
        private final @NotNull ObservableValue<@NotNull LocalDate> max;

        private ForecastDateCell(final @NotNull ObservableValue<@NotNull LocalDate> min, final @NotNull ObservableValue<@NotNull LocalDate> max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public void updateItem(final LocalDate item, final boolean empty) {
            super.updateItem(item, empty);
            setDisable(empty || (item.isBefore(min.getValue()) || item.isAfter(max.getValue())));
        }
    }

}
