package guiByFX.view;

import dataBaseCore.DBCore;
import geneticAlgirithmCore.BasicInfo;
import geneticAlgirithmCore.Population;
import guiByFX.model.ResultModel;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ModelingScreenController {
    public static ObservableList<String> models;
    public static ArrayList<DBCore.SimpleModel> modelsArr;

    private static ResultModel resultModel;

    public TextField mCountAgents;
    public TextField mCountMutation;
    public TextField mCountCross;
    public TextField mCountIterFitness;
    public ComboBox mBasicInfoCombo;
    public RadioButton mButtonByCount;
    public RadioButton mButtonToValue;

    @FXML
    private void initialize() {
        mBasicInfoCombo.setItems(models);
    }

    public void doStartModeling(ActionEvent actionEvent)
            throws SQLException, ClassNotFoundException, IOException {
        int countAgents = 0, countMutation = 0, countCross = 0, countIterFitness = 0;

        try {
            countAgents = Integer.parseInt(mCountAgents.getText());
            countMutation = Integer.parseInt(mCountMutation.getText());
            countCross = Integer.parseInt(mCountCross.getText());
            countIterFitness = Integer.parseInt(mCountIterFitness.getText());
            if (countAgents == 0 || (countMutation == 0 && countCross == 0) || countIterFitness == 0)
                throw new Exception("oi oi");
        } catch (Exception eBlin) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА");
            alert.setHeaderText("Не все поля заполнены!");
            alert.setContentText("Содержимое помое полей должно\nбыть целочисленным и больше нуля!");
            alert.showAndWait();

            return;
        }

        int inx = mBasicInfoCombo.getSelectionModel().getSelectedIndex();
        if (inx == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА");
            alert.setHeaderText("Не выбрана базовая информация!");
            alert.setContentText("Для продолжения моделирования\nвыберете базовую информацию!");
            alert.showAndWait();

            return;
        }

        boolean isByCount = mButtonByCount.isSelected();
        boolean isToValue = mButtonToValue.isSelected();

        if (isByCount == isToValue) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА");
            alert.setHeaderText("Не выбрано услове останова!");
            alert.setContentText("Для продолжения моделирования\nвыберете условие останова генетического алгоритма!");
            alert.showAndWait();

            return;
        }

        DBCore dbCore = new DBCore(null, DBCore.DB_NAME);
        BasicInfo basicInfo = dbCore.getBasicInfoByID(inx);
        dbCore.closeConnection();

        if (basicInfo == null) {
            System.out.println("it is IMPOSIBLE!!!!");
            return;
        }

        Population population = new Population(basicInfo, countAgents, countIterFitness, countCross, countMutation);
        resultModel = new ResultModel();
        if (isByCount) population.doEvolutionProcess(Population.BY_NUM_OF_GENERATION);
        if (isToValue) population.doEvolutionProcess(Population.BY_TARGET_VALUE);
        resultModel.setAgent(population.getAgents().get(0));


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
