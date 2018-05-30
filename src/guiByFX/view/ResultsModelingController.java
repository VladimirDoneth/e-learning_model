package guiByFX.view;

import guiByFX.model.ResultModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.Optional;

public class ResultsModelingController {
    public static ResultModel resultModel;

    public ScrollPane mResultsScroll;

    @FXML
    private void initialize() {
        TextArea textArea = new TextArea();

    }

    public void doCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ВНИМАНИЕ");
        alert.setHeaderText("Вы действительно хотите отменить?");
        alert.setContentText("Модель не сохраниться!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage thisStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            thisStage.hide();
        }
    }

    public void doSave(ActionEvent actionEvent) {
    }

    public void doVisualization(ActionEvent actionEvent) {

    }
}
