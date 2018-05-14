package guiByFX.view;

import guiByFX.Main;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class MainScreen {

    /**
     * method is handler for clicking exit button
     * @param actionEvent
     */
    public void doExitButton(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void doCreateModel(MouseEvent mouseEvent) {
        System.out.println("createModel");
    }

    public void doStartModeling(MouseEvent mouseEvent) {
        System.out.println("startModeling");
    }

    public void doRollupButton(ActionEvent actionEvent) {
        Main.primaryStage.setIconified(true);
    }


    public void doMoreButton(ActionEvent actionEvent) {
        System.out.println("more");
    }

    public void doMovingByMouse(MouseEvent mouseEvent) {
        System.out.println("move"); ///не знаю пока как двигать панель
    }
}
