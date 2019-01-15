package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.MaxFileDataExceededException;
import es.uji.ei1048.meteorologia.model.ResultMode;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.model.converter.CityStringConverter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tornadofx.BindingKt;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static es.uji.ei1048.meteorologia.model.Temperature.Units.CELSIUS;
import static java.lang.String.format;
import static kotlin.collections.CollectionsKt.any;
import static kotlin.collections.CollectionsKt.groupBy;
import static kotlin.collections.SetsKt.setOf;

public class ResultsPane {

    private static final double OFFSET = 7.0;
    private static final double INSETS = 20.0;

    private static final @NotNull String DATA_PROPERTY = "dataProperty"; //NON-NLS
    private static final @NotNull String LABEL_COLOR = "#4040ff"; //NON-NLS
    private static final @NotNull DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"); //NON-NLS

    private final @NotNull ListProperty<@NotNull WeatherData> weatherData = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final @NotNull ObjectProperty<@NotNull ResultMode> resultMode = new SimpleObjectProperty<>();
    private final @NotNull ObjectProperty<@NotNull WeatherManager> manager = new SimpleObjectProperty<>();

    private final @NotNull ObjectProperty<@Nullable WeatherData> selectedData = new SimpleObjectProperty<>();

    @FXML
    private ResourceBundle resources;
    @FXML
    private Label titleCity;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button saveButton;

    public @NotNull ObjectProperty<@NotNull WeatherManager> managerProperty() {
        return manager;
    }

    @FXML
    private void
    initialize() {
        weatherData.addListener(this::updateTabs);
        final @NotNull ObjectBinding<@Nullable WeatherData> binding = weatherData.valueAt(0);
        titleCity.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    final @Nullable WeatherData data = binding.get();
                    return data == null ? "" : CityStringConverter.getInstance().toString(data.getCity());
                },
                binding
        ));

        saveButton.disableProperty().bind(weatherData.emptyProperty());
        manager.setValue(WeatherManager.getInstance());

        // For some reason if I create an object binding it doesn't update, so instead I added a change listener to manually update the `selectedData` ¯\_(ツ)_/¯
        BindingKt.select(tabPane.getSelectionModel().selectedItemProperty(), tab -> tab == null ? null : ((TabPane) tab.getContent()).getSelectionModel().selectedItemProperty())
                .addListener((observable, oldValue, newValue) -> selectedData.set(newValue == null ? null : (WeatherData) newValue.getProperties().get(DATA_PROPERTY)));
    }

    @FXML
    private boolean save() {
        final @Nullable WeatherData data = selectedData.getValue();
        return data != null && save(data);
    }

    private void updateTabs(final @NotNull ListChangeListener.Change<? extends WeatherData> change) {
        while (change.next()) {
            final @NotNull ObservableList<@NotNull Tab> tabs = tabPane.getTabs();

            if (change.wasPermutated()) {
                IntStream.range(0, tabs.size())
                        .mapToObj(it -> ImmutablePair.of(it, change.getPermutation(it)))
                        .filter(it -> !it.getLeft().equals(it.getRight()))
                        .forEach(it -> Collections.swap(tabs, it.getLeft(), it.getRight()));
            } else if (change.wasAdded()) {
                final @NotNull List<Tab> newTabs = groupBy(change.getAddedSubList(), data -> data.getDateTime().toLocalDate(), this::createTab)
                        .entrySet().stream()
                        .map(entry -> {
                            final @NotNull TabPane pane = new TabPane();
                            pane.setSide(Side.BOTTOM);
                            pane.getTabs().addAll(entry.getValue());
                            pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                            return new Tab(entry.getKey().format(DateTimeFormatter.ofPattern("EEE dd")), pane); //NON-NLS
                        })
                        .collect(Collectors.toList());

                tabs.addAll(change.getFrom(), newTabs);
            } else if (change.wasRemoved()) {
                tabs.forEach(it -> ((TabPane) it.getContent()).getTabs().removeIf(tab -> change.getRemoved().stream().anyMatch(data -> tab.getProperties().get(DATA_PROPERTY).equals(data))));
                tabs.removeIf(it -> ((TabPane) it.getContent()).getTabs().isEmpty());
            }
        }
    }

    private @NotNull Tab createTab(final @NotNull WeatherData data) {
        final @NotNull HBox[] rows = {
                createRow(
                        resources.getString("data.date"), setOf(ResultMode.BASIC, ResultMode.ADVANCED),
                        data.getDateTime().format(TIME_FORMATTER)
                ),
                createRow(
                        resources.getString("data.weather"), setOf(ResultMode.BASIC, ResultMode.ADVANCED),
                        data.getWeather().getMain()
                ),
                createRow(
                        resources.getString("data.temperature"), setOf(ResultMode.BASIC),
                        format(resources.getString("data.temperature.value"), data.getTemperature().convertTo(CELSIUS).getCurrent())
                ),
                createRow(
                        resources.getString("data.temperature"), setOf(ResultMode.ADVANCED),
                        format(resources.getString("data.temperature.value.max"), data.getTemperature().convertTo(CELSIUS).getMax()),
                        format(resources.getString("data.temperature.value.current"), data.getTemperature().convertTo(CELSIUS).getCurrent()),
                        format(resources.getString("data.temperature.value.min"), data.getTemperature().convertTo(CELSIUS).getMin())
                ),
                createRow(
                        resources.getString("data.humidity"), setOf(ResultMode.BASIC, ResultMode.ADVANCED),
                        format(resources.getString("data.humidity.value"), data.getHumidity())
                ),
                createRow(
                        resources.getString("data.wind"), setOf(ResultMode.ADVANCED),
                        format(resources.getString("data.wind.value"), data.getWind().getSpeed(), data.getWind().getDegrees())
                ),
                createRow(
                        resources.getString("data.pressure"), setOf(ResultMode.ADVANCED),
                        format(resources.getString("data.pressure.value"), data.getPressure())
                )
        };

        final @NotNull VBox vbox = new VBox(OFFSET, rows);
        vbox.setPadding(new Insets(INSETS, 0.0, INSETS, 0.0));

        final @NotNull ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);

        final @NotNull Tab tab = new Tab(data.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")), scrollPane); //NON-NLS
        tab.getProperties().put(DATA_PROPERTY, data);
        return tab;
    }

    private @NotNull HBox createRow(final @NotNull String name, final @NotNull Set<@NotNull ResultMode> modes, final @NotNull String... values) {
        final @NotNull VBox vboxName = new VBox(OFFSET, new Label(name));
        vboxName.setAlignment(Pos.CENTER_RIGHT);
        vboxName.setPrefWidth(80.0);
        HBox.setHgrow(vboxName, Priority.ALWAYS);

        final @NotNull Label[] lblValues = new Label[values.length];
        for (int i = 0; i < values.length; i++) {
            final @NotNull Label label = new Label(values[i]);
            label.setTextFill(Color.web(LABEL_COLOR));
            lblValues[i] = label;
        }

        final @NotNull VBox vboxValues = new VBox(OFFSET, lblValues);
        vboxValues.setAlignment(Pos.CENTER_LEFT);
        vboxValues.setPrefWidth(100.0);
        HBox.setHgrow(vboxValues, Priority.ALWAYS);

        final @NotNull HBox hbox = new HBox(OFFSET, vboxName, vboxValues);
        hbox.setAlignment(Pos.TOP_CENTER);

        final @NotNull ObservableValue<@NotNull Boolean> binding = Bindings.createBooleanBinding(() -> any(modes, it -> resultMode.get() == it), resultMode);
        hbox.visibleProperty().bind(binding);
        hbox.managedProperty().bind(binding);

        return hbox;
    }

    public boolean save(final @NotNull WeatherData data) {
        try {
            return manager.get().save(data);
        } catch (final MaxFileDataExceededException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("error.save.title"));
            alert.setHeaderText(null);

            alert.setContentText(resources.getString("error.save.content.max"));

            alert.showAndWait();
            return false;
        }
    }

    public void bindWeatherData(final @NotNull ObservableList<@NotNull WeatherData> list) {
        weatherData.bindContentBidirectional(list);
    }

    public void bindResultMode(final @NotNull Property<@NotNull ResultMode> property) {
        resultMode.bindBidirectional(property);
    }

}
