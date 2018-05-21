package guiByFX.view;

import guiByFX.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainScreen {

    /**
     * method is handler for clicking exit button
     * @param actionEvent
     */
    public void doExitButton(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * method is handler for button of create new model, if it clicked main_screen hide
     * and start screen create_model_step1
     * @param mouseEvent
     * @throws IOException
     */
    public void doCreateModel(MouseEvent mouseEvent) throws IOException {
        Stage mainStage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        mainStage.hide();
        Parent root = FXMLLoader.load(getClass().getResource("create_model_step1.fxml"));
        mainStage.setScene(new Scene(root, 640, 300));
        mainStage.show();
    }

    public void doStartModeling(MouseEvent mouseEvent) {
        System.out.println("startModeling");
    }

    public void doRollupButton(ActionEvent actionEvent) {
        Stage appStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        appStage.setIconified(true);
    }


    public void doMoreButton(ActionEvent actionEvent) {
        System.out.println("more");
    }

    public void doMovingByMouse(MouseEvent mouseEvent) {
        System.out.println("move"); ///не знаю пока как двигать панель
    }
}
