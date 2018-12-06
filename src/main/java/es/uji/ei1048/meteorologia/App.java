package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.Coordinates;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.view.ISearchResults;
import es.uji.ei1048.meteorologia.view.LoadWeather;
import es.uji.ei1048.meteorologia.view.RootLayout;
import es.uji.ei1048.meteorologia.view.SearchPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public final class App extends Application {
    private Stage primaryStage;
    private RootLayout rootController;

    public static void main(final @NotNull String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Meteorological Service");
        initRootLayout();
        showSearchPane();
    }

    private void initRootLayout() {
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader(App.class.getResource("/views/RootLayout.fxml")); //NON-NLS
            final @NotNull Parent root = loader.load();

            rootController = loader.getController();

            final @NotNull Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (final @NotNull IOException e) {
            e.printStackTrace(); // FIXME
        }
    }

    private void showSearchPane() {
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader(App.class.getResource("/views/SearchPane.fxml")); //NON-NLS
            final @NotNull Parent root = loader.load();
            final @NotNull SearchPane spController = loader.getController();
            spController.setApp(this);
            rootController.setSearchPane(spController);
            rootController.addPane(root);
        } catch (final @NotNull IOException e) {
            e.printStackTrace(); // FIXME
        }
    }


    public void showSearchResult(final @NotNull String city, final @NotNull WeatherData wd, final boolean advanced) {
        if (rootController.getNumPan() > 1) rootController.clean();
        addResult(city, wd, advanced);
    }

    public @NotNull Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showForecastSearchResult(final String city, final @NotNull List<WeatherData> wdList, final boolean advanced) {
        if (rootController.getNumPan() > 1) rootController.clean();
        for (final WeatherData wd : wdList) {
            addResult(city, wd, advanced);
        }

    }

    private void addResult(final String city, final WeatherData wd, final boolean advanced) {
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader();
            if (advanced) {
                loader.setLocation(App.class.getResource("/views/AdvancedResults.fxml")); //NON-NLS
            } else {
                loader.setLocation(App.class.getResource("/views/BasicResults.fxml")); //NON-NLS
            }
            final @NotNull BorderPane searchResults = loader.load();
            final @NotNull ISearchResults srController = loader.getController();
            srController.showResults(new City(0, city, "España", new Coordinates(42.0, 42.0)), wd);
            srController.setApp(this);
            rootController.addPane(searchResults);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void save(final WeatherData wd) {
        WeatherManager.INSTANCE.save(wd);
    }

    public void saveAll(final List<WeatherData> wdList) {
        for (final WeatherData wd : wdList) {
            save(wd);
        }
    }

    public void showLoadScreen() {
        try {

            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("views/LoadWeather.fxml"));
            final VBox page = loader.load();
            final Stage dialogStage = new Stage();
            dialogStage.setTitle("Load weather");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            final Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            final LoadWeather controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
           /* SaveFile sel = controller.getSel();
            if (sel != null) {
                SaveFile sf = sw.load(sel);
                addResult("Castellon", sf.getWd(), sf.isAdvanced());
            }
*/
        } catch (final IOException e) {
            e.printStackTrace();

        }
    }
}
