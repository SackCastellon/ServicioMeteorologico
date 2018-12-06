package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.api.AccuWeatherApi;
import es.uji.ei1048.meteorologia.api.OpenWeatherApi;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public final class RootLayout {

    @FXML
    private SplitPane sp;


    private SearchPane searchPane;


    public void addPane(final Node node) {
        sp.getItems().add(node);
    }

    public int getNumPan() {
        return sp.getItems().size();
    }

    public void clean() {
        sp.getItems().remove(1, sp.getItems().size() - 1);
    }

    public void setAwApi() {
        searchPane.setApi(new AccuWeatherApi());
    }

    public void setOwApi() {
        searchPane.setApi(new OpenWeatherApi());
    }


    public void setSearchPane(SearchPane searchPane) {
        this.searchPane = searchPane;
    }
}
