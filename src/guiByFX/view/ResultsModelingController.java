package guiByFX.view;

import dataBaseCore.DBCore;
import geneticAlgirithmCore.BasicInfo;
import geneticAlgirithmCore.GenomeOfAgent;
import guiByFX.model.ResultModel;
import javafx.collections.FXCollections;
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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ResultsModelingController {
    public static ResultModel resultModel;

    public TextArea mTextArea;

    @FXML
    private void initialize() {
        BasicInfo basicInfo = resultModel.getAgent().getBasicInfo();
        GenomeOfAgent goa =  resultModel.getAgent();

        String text = "";
        text += "G:\n";
        for (int i = 0; i < basicInfo.A; i++){
            for (int j = 0; j < basicInfo.N; j++) text += (goa.getmG()[i][j] + " ");
            text += "\n";
        }

        text +=("S:\n");
        for (int i = 0; i < basicInfo.D; i++){
            for (int j = 0; j < basicInfo.N; j++) text += (goa.getmS()[i][j] + " ");
            text += "\n";
        }

        text += ("сумманрая интенсивность запуска транзакции j:\n");
        double res1d[] = basicInfo.calcLoadOfTransaction();
        for (double x: res1d) {
            text += (x + " ");
        }
        text += "\n\nсуммарная интенсивность запуска приложения k:\n";
        res1d = basicInfo.calcLoadOFApp();
        for (double x: res1d) {
            text += (x + " ");
        }
        text += ("\n\nрассчитаем работу функционирования" +
                "\nтранзакции j на информационных узлах n и m\n");
        int[][][] res3i = goa.workOfApp();
        for (int e = 0; e < basicInfo.E; e++) {
            for (int n = 0; n < basicInfo.N; n++) {
                for (int m = 0; m < basicInfo.N; m++) {
                    text += (res3i[e][n][m] + " ");
                }
                text += "\n";
            }
            text += "\n";
        }
        text += "\nопределим объемы данных передаваемых из хранилища данных:\n";
        res3i = goa.workOfStorage();
        for (int e = 0; e < basicInfo.E; e++) {
            for (int n = 0; n < basicInfo.N; n++) {
                for (int m = 0; m < basicInfo.N; m++) {
                    text += (res3i[e][n][m] + " ");
                }
                text += "\n";
            }
            text += "\n";
        }

        text += ("\nОбъемы данных в сети пределяются так:\n");
        res3i = goa.dataInNetwork();
        for (int e = 0; e < basicInfo.E; e++) {
            for (int n = 0; n < basicInfo.N; n++) {
                for (int m = 0; m < basicInfo.N; m++) {
                    text += (res3i[e][n][m] + " ");
                }
                text += "\n";
            }
            text += "\n";
        }

        text += ("\nтеперь можно посторить матрицу интенсивносетй обмена между" +
                "\nинфо узлами сети при выполнении транзакции j\n");
        double res3d[][][] = goa.loadOfNodesByTransactions();
        for (int e = 0; e < basicInfo.E; e++) {
            for (int n = 0; n < basicInfo.N; n++) {
                for (int m = 0; m < basicInfo.N; m++) {
                    int ttt = (int) res3d[e][n][m];
                    text += (ttt + " ");
                }
                text += "\n";
            }
            text += "\n";
        }

        text += ("\nсоответственно, матрица нагрузки на" +
                "\nинформационные узлы определяется так\n");
        double res2d[][] = goa.loadOfNetwork();
        for (int e = 0; e < basicInfo.N; e++) {
            for (int m = 0; m < basicInfo.N; m++) {
                int ttt = (int) res2d[e][m];
                text += (ttt + " ");
            }
            text += "\n";
        }

        text += ("\nопределим среднюю нагрузку одного узла сети\n");
        double resd = goa.averageLoad();
        text += (resd + "\n");

        text += ("\nсумма отклонений нагрузки от средней\n");
        resd = goa.fitness();
        text += (resd + "\n");

        text += ("\n/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/\n\n");
        text += ("интенсивность потока запросов на запуск приложения i" +
                "\nустановленного на узле n\n");
        res2d = goa.intensityOfStartApp();
        for (int a = 0; a < basicInfo.A; a++) {
            for (int n = 0; n < basicInfo.N; n++) {
                int ttt = (int) res2d[a][n];
                text += (ttt + " ");
            }
            text += "\n";
        }

        text += ("\nзагрузка узлов с элементами хранилища даннхы: транзакия j" +
                "\nузел n, фрагмент d\n");
        res3d = goa.loadNodesStorage();
        for (int j = 0; j < basicInfo.E; j++) {
            for (int d = 0; d < basicInfo.D; d++){
                for (int n = 0; n < basicInfo.N; n++) {
                    int ttt = (int) res3d[j][d][n];
                    text += (ttt + " ");
                }
                text += "\n";
            }
            text += "\n";
        }

        text += ("интенсивность закпросов к хранилищу\n");
        res2d = goa.intensityStorage();
        for (int d = 0; d < basicInfo.D; d++) {
            for (int n = 0; n < basicInfo.N; n++) {
                int ttt = (int) res2d[d][n];
                text += (ttt + " ");
            }
            text += "\n";
        }

        mTextArea.setText(text);
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
        try {
            DBCore dbCore = new DBCore(null, DBCore.DB_NAME);
            dbCore.saveToDBResult(resultModel.getIdModel(),resultModel.getAgent(), "комментарий временный");
        } catch (Exception errorlala) {
            errorlala.printStackTrace();
            System.out.println("чет не сохранилось...");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ВЫПОЛНЕНО");
        alert.setHeaderText("Данные синтеза успешно сохранены");
        alert.showAndWait();

        Stage thisStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        thisStage.hide();
    }

    public void doVisualization(ActionEvent actionEvent) throws IOException {
        Stage childStage = new Stage();
        childStage.setAlwaysOnTop(false);
        childStage.initStyle(StageStyle.UTILITY);

        GraphViewControler.resultModel = resultModel;

        Parent root = FXMLLoader.load(getClass().getResource("graph_view.fxml"));
        childStage.setScene(new Scene(root, 640, 600));
        childStage.initOwner((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
        childStage.showAndWait();
    }
}
