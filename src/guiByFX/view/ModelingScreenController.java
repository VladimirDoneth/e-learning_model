package guiByFX.view;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class ModelingScreenController {
    public static ObservableList<String> models;

    public TextField mCountAgents;
    public TextField mCountMutation;
    public TextField mCountCross;
    public TextField mCountIterFitness;
    public ComboBox mBasicInfoCombo;

    @FXML
    private void initialize() {
        mBasicInfoCombo.setItems(models);
    }

    public void doStartModeling(ActionEvent actionEvent) {

    }

    public void doCancelModeling(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ВНИМАНИЕ");
        alert.setHeaderText("Вы действительно хотите завершить?");
        alert.setContentText("Процесс моделирования завершится!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage mainStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            mainStage.close();
        }
    }
}
