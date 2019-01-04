package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.view.LoadWeather;
import es.uji.ei1048.meteorologia.view.ResultsPane;
import es.uji.ei1048.meteorologia.view.RootLayout;
import es.uji.ei1048.meteorologia.view.SearchPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ResourceBundle;

public final class App extends Application {

    private static final @NotNull Logger logger = LogManager.getLogger(App.class);

    private Stage primaryStage;
    private RootLayout rootController;

    public static void main(final @NotNull String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        logger.debug("Starting application"); //NON-NLS

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Meteorological Service");
        this.primaryStage.setMinWidth(525.0);
        this.primaryStage.setMinHeight(475.0);

        initRootLayout();
        showSearchPane();
        showResultPane();

        rootController.sync();

        this.primaryStage.show();
    }

    private void initRootLayout() {
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader(App.class.getResource("/views/RootLayout.fxml")); //NON-NLS
            loader.setResources(ResourceBundle.getBundle("bundles/RootLayout")); //NON-NLS
            final @NotNull Parent root = loader.load();

            rootController = loader.getController();

            final @NotNull Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (final @NotNull IOException e) {
            logger.error("Error loading root layout", e); //NON-NLS
        }
    }

    private void showSearchPane() {
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader(App.class.getResource("/views/SearchPane.fxml")); //NON-NLS
            loader.setResources(ResourceBundle.getBundle("bundles/SearchPane")); //NON-NLS
            final @NotNull Parent root = loader.load();
            final @NotNull SearchPane controller = loader.getController();
            controller.setApp(this);
            rootController.setSearchPane(root, controller);
        } catch (final @NotNull IOException e) {
            logger.error("Error loading search pane", e); //NON-NLS
        }
    }

    private void showResultPane() {
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader(App.class.getResource("/views/ResultsPane.fxml")); //NON-NLS
            loader.setResources(ResourceBundle.getBundle("bundles/ResultsPane")); //NON-NLS
            final @NotNull Parent root = loader.load();
            final @NotNull ResultsPane controller = loader.getController();

            rootController.setResultPane(root, controller);
        } catch (final @NotNull IOException e) {
            logger.error("Error loading result pane", e); //NON-NLS
        }
    }

    public void showLoadWindow() {
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader(App.class.getResource("/views/LoadWeather.fxml")); //NON-NLS
            final @NotNull ResourceBundle bundle = ResourceBundle.getBundle("bundles/LoadWeather"); //NON-NLS
            loader.setResources(bundle); //NON-NLS

            final @NotNull Stage stage = new Stage();
            stage.setTitle(bundle.getString("title"));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);

            final @NotNull Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            final @NotNull LoadWeather controller = loader.getController();
            controller.setStage(stage);

            stage.showAndWait();

            if (controller.isSuccess()) {
                rootController.setWeatherData(controller.getWeatherData());
                rootController.setResultMode(controller.getResultMode());
            }
        } catch (final @NotNull IOException e) {
            logger.error("Error loading load pane", e); //NON-NLS
        }
    }
}
