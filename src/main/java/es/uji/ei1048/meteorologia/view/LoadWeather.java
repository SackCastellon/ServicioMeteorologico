package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.SaveFile;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class LoadWeather {

    @FXML
    private ListView<SaveFile> saveList;

    public void setDialogStage(Stage dialogStage) {

    }

    public SaveFile getSel() {
        return saveList.getSelectionModel().getSelectedItem();
    }
}
