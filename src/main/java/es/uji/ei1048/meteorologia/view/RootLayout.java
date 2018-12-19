package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.service.AccuWeather;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import org.controlsfx.control.StatusBar;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

import static es.uji.ei1048.meteorologia.util.Utils.bindToToggleGroup;

public final class RootLayout {

    private static final @NotNull String PROVIDER_PROPERTY = "providerProperty"; //NON-NLS

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
    }

    public void setResultPane(final @NotNull Parent root, final @NotNull ResultsPane controller) {
        splitPane.getItems().set(1, root);
        SplitPane.setResizableWithParent(root, true);
        resultsController = controller;
    }

    public void sync() {
        resultsController.bindWeatherData(searchController.weatherDataProperty());
        resultsController.bindResultMode(searchController.resultModeProperty());
    }
}
