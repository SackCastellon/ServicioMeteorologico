package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.Coordinates;
import es.uji.ei1048.meteorologia.model.SaveWeather;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.view.ISearchResults;
import es.uji.ei1048.meteorologia.view.RootLayout;
import es.uji.ei1048.meteorologia.view.SearchPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public final class App extends Application {
    private Stage primaryStage;
    private RootLayout rootController;
    private SaveWeather sw;
    public static void main(final @NotNull String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Meteorological Service");
        this.sw = new SaveWeather();
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


    public void showSearchResults(final @NotNull String city, final @NotNull WeatherData wd, final boolean advanced) {
        if (rootController.getNumPan() > 1) rootController.clean();
        addResult(city, wd, advanced);
    }

    public @NotNull Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showForecastSearchResult(final String city, final @NotNull List<WeatherData> wdList, final boolean advanced) {
        if (rootController.getNumPan() > 1) rootController.clean();
        for (WeatherData wd : wdList) {
            addResult(city, wd, advanced);
        }

    }

    private void addResult(String city, WeatherData wd, boolean advanced) {
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader();
            if (advanced) {
                loader.setLocation(App.class.getResource("/views/AdvancedResults.fxml")); //NON-NLS
            } else {
                loader.setLocation(App.class.getResource("/views/BasicResults.fxml")); //NON-NLS
            }
            final @NotNull BorderPane searchResults = loader.load();
            final @NotNull ISearchResults srController = loader.getController();
            srController.showResults(new City(0, city, "España", new Coordinates(333, 333)), wd);
            rootController.addPane(searchResults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(WeatherData wd) {
        sw.save(wd);
    }

    public void saveAll(List<WeatherData> wdList) {
        for (WeatherData wd : wdList
        ) {
            save(wd);
        }
    }
}
