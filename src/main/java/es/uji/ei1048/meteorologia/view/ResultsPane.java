package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.view.SearchPane.ResultMode;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import kotlin.collections.CollectionsKt;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static es.uji.ei1048.meteorologia.model.Temperature.Units.CELSIUS;
import static kotlin.collections.CollectionsKt.groupBy;

public final class ResultsPane {

    private static final @NotNull String DATA_PROPERTY = "dataProperty"; //NON-NLS
    private static final @NotNull String LABEL_COLOR = "#4040ff"; //NON-NLS

    private final @NotNull ListProperty<@NotNull WeatherData> weatherData = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final @NotNull ObjectProperty<@NotNull ResultMode> resultMode = new SimpleObjectProperty<>();

    @FXML
    private Label title;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button saveButton;

    private static @NotNull Tab createTab(final @NotNull WeatherData data) {
        final @NotNull ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.SOMETIMES);
        cc1.setHalignment(HPos.RIGHT);

        final @NotNull ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.SOMETIMES);
        cc2.setHalignment(HPos.LEFT);

        final GridPane gridPane = new GridPane();

        gridPane.getColumnConstraints().setAll(cc1, cc2);
        gridPane.setHgap(7.0);
        gridPane.setVgap(7.0);
        gridPane.setPadding(new Insets(15.0));

        final @NotNull List<@NotNull Label[]> rows = CollectionsKt.listOf(
                createLabels("Time:", data.getDateTime().toString()),
                createLabels("Weather:", data.getWeather().getMain()),
                createLabels("Temperature:", String.format("%.2f ºC", data.getTemperature().convertTo(CELSIUS).getCurrent())),
                createLabels("Humidity:", String.format("%.2f RH%%", data.getHumidity())),
                createLabels("Wind:", String.format("%.2f Km/h (%.2fº)", data.getWind().getSpeed(), data.getWind().getDegrees())),
                createLabels("Pressure:", String.format("%.2f hPa", data.getPressure()))
        );

        int row = 0;
        for (int i = 0; i < rows.size(); i++, row++) {
            final @NotNull Label[] labels = rows.get(i);
            gridPane.add(labels[0], 0, row, 1, labels.length - 1);
            for (int j = 1; j < labels.length; j++, row++) {
                gridPane.add(labels[j], 1, row);
            }
        }

        final @NotNull ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);

        final @NotNull Tab tab = new Tab(data.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")), scrollPane);
        tab.getProperties().put(DATA_PROPERTY, data.hashCode());
        return tab;
    }

    private static @NotNull Label[] createLabels(final @NotNull String name, final @NotNull String... values) {
        final @NotNull Label[] labels = new Label[values.length + 1];
        labels[0] = new Label(name);
        for (int i = 0; i < values.length; ) {
            final Label label = new Label(values[i]);
            label.setTextFill(Color.web(LABEL_COLOR));
            labels[++i] = label;
        }
        return labels;
    }

    @FXML
    private void
    initialize() {
        weatherData.addListener(this::updateTabs);
    }

    public void showResults(final @NotNull WeatherData wd) {
//        this.wd = wd;
//        final City city = wd.getCity();
//        cityRes.setText(city.getName());
//        weatherRes.setText(wd.getWeather().getDescription().toUpperCase());
//        rhRes.setText(wd.getHumidity() + " RH%");
//        timeRes.setText(wd.getDateTime().toString());
//        final Temperature temp = wd.getTemperature();
//        tempRes.setText("Current: " + temp.getCurrent() + "°C " + "Max: " + temp.getMax() + "°C " + "Min: " + temp.getCurrent() + "°C");
//        final Wind wind = wd.getWind();
//        windRes.setText(" " + wind.getSpeed() + " KM/h Degrees: " + wind.getDegrees());
//        pressureRes.setText(wd.getPressure() + "atm");
    }

    public void bindWeatherData(final @NotNull ObservableList<@NotNull WeatherData> list) {
        weatherData.bindContent(list);
    }

    public void bindResultMode(final @NotNull ObservableValue<@NotNull ResultMode> property) {
        resultMode.bind(property);
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
                final @NotNull List<Tab> newTabs = groupBy(change.getAddedSubList(), data -> data.getDateTime().toLocalDate(), ResultsPane::createTab)
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
                tabs.forEach(it -> ((TabPane) it.getContent()).getTabs().removeIf(tab -> change.getRemoved().stream().anyMatch(data -> tab.getProperties().get(DATA_PROPERTY).equals(data.hashCode()))));
                tabs.removeIf(it -> ((TabPane) it.getContent()).getTabs().isEmpty());
            }
        }
    }
}
