package guiByFX.view;

import dataBaseCore.DBCore;
import geneticAlgirithmCore.BasicInfo;
import geneticAlgirithmCore.GenomeOfAgent;
import guiByFX.model.TableViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainScreen {

    public TableColumn mModelColum;
    public TableColumn mMarkColum;
    public TableView mTable;
    public ImageView mSettingsButton;
    public ImageView mHelpButton;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException, IOException {
        try {
            mSettingsButton.setImage(new Image(new FileInputStream("src/data/icon_settings.png")));
            mHelpButton.setImage(new Image(new FileInputStream("src/data/icon_help.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObservableList<TableViewModel> modelsOfResult = FXCollections.observableArrayList();
        mModelColum.setCellValueFactory(new PropertyValueFactory<TableViewModel, String>("name"));
        mMarkColum.setCellValueFactory(new PropertyValueFactory<TableViewModel, String>("result"));


        DBCore dbCore = new DBCore(null, DBCore.DB_NAME);
        ArrayList<DBCore.SimpleModel> simpleModels = dbCore.getSimpleModels();

        for (int i = 0; i < simpleModels.size(); i++) {
            ArrayList<Integer> ids = dbCore.getIdRelultsByModel(simpleModels.get(i).idModel);
            for (int j = 0; j < ids.size(); j++) {
                GenomeOfAgent genomeOfAgent = dbCore.getGenomeOfAgentByModelID(simpleModels.get(i).idModel, ids.get(j));
                TableViewModel tableViewModel = new TableViewModel(genomeOfAgent, simpleModels.get(i).gerateStringForComboBox());
                //modelsOfResult.add(tableViewModel); //в общем дичь с этими таблицами, не работает
            }
        }

        mTable.setItems(modelsOfResult);
    }

    /**
     * method is handler for clicking exit button
     * @param actionEvent
     */
    public void doExitButton(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * method is handler for button of 'start modeling', if it clicked main_screen not hide
     * and start screen 'modeling_screen_start.fxml' and user can do modeling process
     * @param mouseEvent
     */
    public void doStartModeling(MouseEvent mouseEvent) throws IOException, SQLException, ClassNotFoundException {
        Stage childStage = new Stage();
        childStage.setAlwaysOnTop(false);
        childStage.initStyle(StageStyle.UTILITY);

        ModelingScreenController.models = FXCollections.observableArrayList();
        DBCore dbCore = new DBCore(null, DBCore.DB_NAME);
        ArrayList<DBCore.SimpleModel> simpleModels = dbCore.getSimpleModels();
        dbCore.closeConnection();

        ModelingScreenController.modelsArr = simpleModels;
        for (int i = 0; i < simpleModels.size(); i++) {
            ModelingScreenController.models.add(i, simpleModels.get(i).gerateStringForComboBox());
        }

        Parent root = FXMLLoader.load(getClass().getResource("modeling_screen_start.fxml"));
        childStage.setScene(new Scene(root, 640, 400));
        childStage.initOwner((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow());
        childStage.showAndWait();
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
