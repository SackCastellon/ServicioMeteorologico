package es.uji.ei1048.meteorologia;

import es.uji.ei1048.meteorologia.view.RootLayout;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private RootLayout rlController;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Meteorological Service");

        initRootLayout();

        showSearchPane();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/views/RootLayout.fxml"));
            rootLayout = loader.load();
            rlController = loader.getController();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showSearchPane() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/views/SearchPane.fxml"));
            VBox searchPane = loader.load();

            rlController.addPane(searchPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(final String[] args) {
        Application.launch(args);
    }
}
