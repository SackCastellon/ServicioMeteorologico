package es.uji.ei1048.meteorologia.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public final class RootLayout {

    @FXML
    private SplitPane sp;

    public void addPane(final Node node) {
        sp.getItems().add(node);
    }

    public int getNumPan() {
        return sp.getItems().size();
    }

    public void clean() {
        sp.getItems().remove(1);
    }
}
