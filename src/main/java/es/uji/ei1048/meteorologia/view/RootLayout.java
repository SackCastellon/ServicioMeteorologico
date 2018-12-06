package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.service.AccuWeather;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public final class RootLayout {

    @FXML
    private HBox sp;

    private WeatherManager sw;

    private SearchPane searchPane;


    public void addPane(final Node node) {
        sp.getChildren().add(node);
    }

    public int getNumPan() {
        return sp.getChildren().size();
    }

    public void clean() {
        sp.getChildren().remove(1, sp.getChildren().size());
        System.out.println("Limpio");
        System.out.println(getNumPan());
    }

    public void setAwApi() {
        searchPane.setApi(new AccuWeather());
    }

    public void setOwApi() {
        searchPane.setApi(new OpenWeather());
    }

    public void setSearchPane(final SearchPane searchPane) {
        this.searchPane = searchPane;
    }
}
