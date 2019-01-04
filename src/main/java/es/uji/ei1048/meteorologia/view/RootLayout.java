package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.ResultMode;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.service.AccuWeather;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import org.controlsfx.control.StatusBar;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ResourceBundle;

import static es.uji.ei1048.meteorologia.util.Utils.bindToToggleGroup;

public final class RootLayout {

    private static final @NotNull String PROVIDER_PROPERTY = "providerProperty"; //NON-NLS
    private final @NotNull ListProperty<@NotNull WeatherData> weatherData = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final @NotNull ObjectProperty<@NotNull ResultMode> resultMode = new SimpleObjectProperty<>();
    @FXML
    private ResourceBundle resources;
    @FXML
    private ToggleGroup groupProviders;
    @FXML
    private RadioMenuItem serviceOpenWeather;
    @FXML
    private RadioMenuItem serviceAccuWeather;
    @FXML
    private SplitPane splitPane;
    @FXML
    private StatusBar statusBar; // TODO
    private SearchPane searchController;
    private ResultsPane resultsController;

    @FXML
    private void initialize() {
        statusBar.setText(resources.getString("status.loaded"));

        splitPane.getItems().setAll(new Pane(), new Pane());

        serviceOpenWeather.getProperties().put(PROVIDER_PROPERTY, OpenWeather.getInstance());
        serviceAccuWeather.getProperties().put(PROVIDER_PROPERTY, AccuWeather.getInstance());
    }

    public void setSearchPane(final @NotNull Parent root, final @NotNull SearchPane controller) {
        splitPane.getItems().set(0, root);
        SplitPane.setResizableWithParent(root, false);
        searchController = controller;

        splitPane.setDividerPositions(0.0);

        bindToToggleGroup(searchController.providerProperty(), groupProviders, PROVIDER_PROPERTY);
        searchController.statusProperty().bindBidirectional(statusBar.textProperty());
    }

    public void setResultPane(final @NotNull Parent root, final @NotNull ResultsPane controller) {
        splitPane.getItems().set(1, root);
        SplitPane.setResizableWithParent(root, true);
        resultsController = controller;
    }

    public void sync() {
        resultsController.bindWeatherData(weatherData);
        resultsController.bindResultMode(resultMode);
        searchController.bindWeatherData(weatherData);
        searchController.bindResultMode(resultMode);
    }

    public void setWeatherData(final @NotNull List<@NotNull WeatherData> data) {
        weatherData.setAll(data);
    }

    public void setResultMode(final @NotNull ResultMode mode) {
        resultMode.set(mode);
    }

    public void aboutPopup() {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString("alert.about.title"));
        alert.setHeaderText(null);
        alert.setContentText(resources.getString("alert.about.content"));

        alert.showAndWait();
    }
}
