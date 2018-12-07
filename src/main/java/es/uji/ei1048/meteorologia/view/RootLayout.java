package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.WeatherManager;
import es.uji.ei1048.meteorologia.service.AccuWeather;
import es.uji.ei1048.meteorologia.service.OpenWeather;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public final class RootLayout {

    @FXML
    private SplitPane sp;

    private WeatherManager sw;

    private SearchPane searchPane;


    public void addPane(final Node node) {
        sp.getItems().add(node);
        for (SplitPane.Divider div : sp.getDividers()
        ) {
            div.setPosition(div.getPosition() + 1);
        }
    }

    public int getNumPan() {
        return sp.getItems().size();
    }

    public void clean() {
        sp.getItems().remove(1, sp.getItems().size());
        System.out.println("Limpio");
        System.out.println(getNumPan());
    }

    public void setAwApi() {
        searchPane.setService(new AccuWeather());
    }

    public void setOwApi() {
        searchPane.setService(new OpenWeather());
    }

    public void setSearchPane(final SearchPane searchPane) {
        this.searchPane = searchPane;
    }
}
