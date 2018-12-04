package es.uji.ei1048.meteorologia.view;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;

public class RootLayout {

    @FXML
    SplitPane sp;

    public void addPane(Pane pane){
        sp.getItems().add(pane);
    }

}
