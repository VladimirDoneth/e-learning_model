package guiByFX.view;

import geneticAlgirithmCore.ParamOfApp;
import geneticAlgirithmCore.Transaction;
import guiByFX.model.DataModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class CreateModelController {
    public static DataModel dataModel;
    private static boolean isNotMade = true;
    private static TextField [][] intensityText;
    private static CheckBox [] usedApp;
    private static CheckBox [] usedDim;
    private static CheckBox [] usedUsers;
    private static CheckBox [][] usingOrder;
    private static int numOfTransaction;
    private boolean [] isFilledTrs;
    public static ParamOfApp[] paramOfApps;

    /**
     * this is fields by create_model_step1.fxml
     */
    public TextArea textArea;
    public DatePicker datePicker;

    /**
     * this is fields by create_model_step2.fxml
     */
    public TextField countAppTextField;
    public TextField countNodeTextField;
    public TextField countUsersTextField;
    public TextField countTransactionTextField;
    public TextField countDimTextField;

    /**
     * this is fields by create_model_step3.fxml
     */
    public ScrollPane scrollIntensity;

    /**
     * this is fields by create_model_step4.fxml
     */
    public Label myIndexation;
    public ScrollPane usedAppScrollPane;
    public ScrollPane usedDimScrollPane;
    public ScrollPane usedUsersScrollPane;
    public ScrollPane orderRunScrollPane;


    public void doCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ВНИМАНИЕ");
        alert.setHeaderText("Вы действительно хотите завершить?");
        alert.setContentText("Добавляемая модель не сохраниться!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            CreateModelController.isNotMade = true;
            Stage mainStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            mainStage.hide();
            Parent root = FXMLLoader.load(getClass().getResource("main_screen.fxml"));
            mainStage.setScene(new Scene(root, 760, 480));
            mainStage.show();
        }
    }

    public void doNextOne(ActionEvent actionEvent) throws IOException {
        CreateModelController.dataModel = new DataModel();
        if (!"".equals(textArea.getText()) && !"".equals(datePicker.getEditor().getText())) {
            CreateModelController.dataModel.setAboutModel(textArea.getText());
            CreateModelController.dataModel.setDateCreate(datePicker.getEditor().getText());

            Stage mainStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            mainStage.hide();
            Parent root = FXMLLoader.load(getClass().getResource("create_model_step2.fxml"));
            mainStage.setScene(new Scene(root, 640, 300));
            mainStage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА");
            alert.setHeaderText("Не все поля заполнены!");
            alert.showAndWait();
        }
    }

    public void doNextTwo(ActionEvent actionEvent) throws IOException {
        int A = 0, U = 0, N = 0, E = 0, D = 0;
        boolean isOk = false;
        try {
            A = Integer.parseInt(countAppTextField.getText());
            U = Integer.parseInt(countUsersTextField.getText());
            N = Integer.parseInt(countNodeTextField.getText());
            E = Integer.parseInt(countTransactionTextField.getText());
            D = Integer.parseInt(countDimTextField.getText());
            if ((A > 0) && (U > 0) && (N > 0) && (E > 0) && (D > 0))
                isOk = true;
        } catch (Exception e) {
            System.out.println("What are rabbit nose???");
        }

        if (isOk) {
            CreateModelController.dataModel.getBasicInfo().A = A;
            CreateModelController.dataModel.getBasicInfo().U = U;
            CreateModelController.dataModel.getBasicInfo().N = N;
            CreateModelController.dataModel.getBasicInfo().E = E;
            CreateModelController.dataModel.getBasicInfo().D = D;

            Stage mainStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            mainStage.hide();
            Parent root = FXMLLoader.load(getClass().getResource("create_model_step3.fxml"));
            mainStage.setScene(new Scene(root, 640, 400));
            mainStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА");
            alert.setHeaderText("Не все поля заполнены!");
            alert.setContentText("Поля должны содержать целые числа,\nбольшие чем ноль!!!");
            alert.showAndWait();
        }
    }


    public void doNextThree(ActionEvent actionEvent) throws IOException {
        boolean isOK = true;
        double intensity[][] = new double[dataModel.getBasicInfo().E][dataModel.getBasicInfo().U];

        System.out.println(dataModel.getBasicInfo().E + " " + dataModel.getBasicInfo().U);
        try {
            for (int i = 0; i < dataModel.getBasicInfo().E; i++) {
                for (int j = 0; j < dataModel.getBasicInfo().U; j++) {
                    intensity[i][j] = Double.parseDouble(intensityText[i][j].getText());
                    if (intensity[i][j] < 0) isOK = false;
                }
            }
        } catch (Exception wtf) {
            System.err.println("You have error!");
            isOK = false;
        }

        if (isOK) {
            isNotMade = true;
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            mainStage.hide();
            Parent root = FXMLLoader.load(getClass().getResource("create_model_step4.fxml"));
            mainStage.setScene(new Scene(root, 640, 600));
            mainStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА");
            alert.setHeaderText("Не все поля заполнены!");
            alert.setContentText("Поля должны содержать числа,\nне меньше чем ноль!!!");
            alert.showAndWait();
        }
    }

    public void doActivateFields(MouseEvent mouseEvent) {
        if (isNotMade) {
            isNotMade = false;
            GridPane gridPane = new GridPane();
            gridPane.setPrefSize(55 * (CreateModelController.dataModel.getBasicInfo().E + 1),
                    12 * (CreateModelController.dataModel.getBasicInfo().U + 1));
            for (int i = 1; i <= CreateModelController.dataModel.getBasicInfo().E; i++) {
                Label label = new Label();
                label.setPrefSize(55, 12);
                label.setText(i + "");
                gridPane.add(label, i, 0);
            }
            for (int i = 1; i <= CreateModelController.dataModel.getBasicInfo().U; i++) {
                Label label = new Label();
                label.setText(i + "");
                gridPane.add(label, 0, i);
            }

            intensityText = new TextField[dataModel.getBasicInfo().E][dataModel.getBasicInfo().U];
            for (int i = 0; i < intensityText.length; i++) {
                for (int j = 0; j < intensityText[0].length; j++) {
                    TextField textField = new TextField();
                    textField.setPrefSize(55, 12);
                    intensityText[i][j] = textField;
                }
            }

            for (int i = 1; i <= intensityText.length; i++) {
                for (int j = 1; j <= intensityText[0].length; j++) {
                    gridPane.add(intensityText[i-1][j-1], i, j);
                }
            }

            scrollIntensity.setContent(gridPane);
        }
    }

    public void doActivateFieldsByTrs(MouseEvent mouseEvent) {
        if (isNotMade) {
            isNotMade = false;
            isFilledTrs = new boolean[dataModel.getBasicInfo().E];
            numOfTransaction = 0;
            myIndexation.setText((numOfTransaction + 1) + " / " + dataModel.getBasicInfo().E);
            dataModel.getBasicInfo().transactions = new Transaction[dataModel.getBasicInfo().E];

            //fill usedAppScrollPane;
            usedApp = new CheckBox[CreateModelController.dataModel.getBasicInfo().A];
            GridPane gridPaneApp = new GridPane();

            for (int i = 0; i < CreateModelController.dataModel.getBasicInfo().A; i++) {
                usedApp[i] = new CheckBox();
                usedApp[i].setPrefSize(35,12);
                Label label = new Label();
                label.setPrefSize(35, 12);
                label.setText((i + 1) +"");
                gridPaneApp.add(label, i, 0);
                gridPaneApp.add(usedApp[i], i, 1);
            }

            usedAppScrollPane.setContent(gridPaneApp);


            //fill usedDimScrollPane
            usedDim = new CheckBox[CreateModelController.dataModel.getBasicInfo().D];
            GridPane gridPaneDim = new GridPane();

            for (int i = 0; i < CreateModelController.dataModel.getBasicInfo().D; i++) {
                usedDim[i] = new CheckBox();
                usedDim[i].setPrefSize(35, 12);
                Label label = new Label();
                label.setPrefSize(35, 12);
                label.setText((i + 1) +"");
                gridPaneDim.add(label, i, 0);
                gridPaneDim.add(usedDim[i], i, 1);
            }

            usedDimScrollPane.setContent(gridPaneDim);

            //fill usedUsersScrollPane
            GridPane gridPaneUsers = new GridPane();
            usedUsers = new CheckBox[CreateModelController.dataModel.getBasicInfo().U];

            for (int i = 0; i < CreateModelController.dataModel.getBasicInfo().U; i++) {
                usedUsers[i] = new CheckBox();
                usedUsers[i].setPrefSize(35, 12);
                Label label = new Label();
                label.setPrefSize(35, 12);
                label.setText((i + 1) +"");
                gridPaneUsers.add(label, i, 0);
                gridPaneUsers.add(usedUsers[i], i, 1);
            }

            usedUsersScrollPane.setContent(gridPaneUsers);

            //fill orderRunScrollPane
            GridPane gridUsingOrder = new GridPane();
            usingOrder = new CheckBox[CreateModelController.dataModel.getBasicInfo().A][CreateModelController.dataModel.getBasicInfo().A];

            for (int i = 0; i < CreateModelController.dataModel.getBasicInfo().A; i++) {
                Label label1 = new Label();
                Label label2 = new Label();
                label1.setText((i+1) + "");
                label2.setText((i+1) + "");
                label1.setPrefSize(35, 12);
                label2.setPrefSize(35, 12);

                gridUsingOrder.add(label1, i + 1, 0);
                gridUsingOrder.add(label2, 0, i + 1);
            }

            for (int i = 0; i < CreateModelController.dataModel.getBasicInfo().A; i++) {
                for (int j = 0; j < CreateModelController.dataModel.getBasicInfo().A; j++) {
                    usingOrder[i][j] = new CheckBox();
                    usingOrder[i][j].setPrefSize(35, 12);

                    gridUsingOrder.add(usingOrder[i][j], i + 1, j + 1);
                }
            }

            orderRunScrollPane.setContent(gridUsingOrder);
        }
    }

    public void doNextTrs(MouseEvent mouseEvent) {
        doNextPerious(true);
    }

    public void doPerviousTrs(MouseEvent mouseEvent) {
        doNextPerious(false);
    }

    public void doFillParamApp(MouseEvent mouseEvent) throws IOException {
        int usedAppInt[] = new int[dataModel.getBasicInfo().A];
        int sumTest = 0;
        for (int i = 0; i < dataModel.getBasicInfo().A; i++) {
            if (usedApp[i].isSelected()) usedAppInt[i] = 1;
            else usedAppInt[i] = 0;
            sumTest += usedAppInt[i];
        }
        if (sumTest == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА ДАННЫХ");
            alert.setHeaderText("Транзакция не может работать без приложений!");
            alert.setContentText("Выберете хотябы одно приложение для\nработы транзакции!");
            alert.showAndWait();
            return;
        }

        int usedDimInt[] = new int[dataModel.getBasicInfo().D];
        for (int i = 0; i < dataModel.getBasicInfo().D; i++) {
            if (usedDim[i].isSelected()) usedDimInt[i] = 1;
            else usedDimInt[i] = 0;
        }

        CreateModelController5.usedAppInt = usedAppInt;
        CreateModelController5.usedDimInt = usedDimInt;

        //тут дописываем код по вызову след окна
        Stage childStage = new Stage();
        childStage.setAlwaysOnTop(true);
        childStage.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("create_model_step5.fxml"));
        childStage.setScene(new Scene(root));
        childStage.initOwner((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow());
        childStage.showAndWait();
    }

    public void doSave(ActionEvent actionEvent) {

    }

    private void doNextPerious(boolean isNext) {
        int usedAppInt[] = new int[dataModel.getBasicInfo().A];
        int sumTest = 0;
        for (int i = 0; i < dataModel.getBasicInfo().A; i++) {
            if (usedApp[i].isSelected()) usedAppInt[i] = 1;
            else usedAppInt[i] = 0;
            sumTest += usedAppInt[i];
        }
        if (sumTest == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА ДАННЫХ");
            alert.setHeaderText("Транзакция не может работать без приложений!");
            alert.setContentText("Выберете хотябы одно приложение для\nработы транзакции!");
            alert.showAndWait();
            return;
        }

        if (paramOfApps == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("НЕ ВСЕ ДАННЫЕ ЗАПОЛНЕНЫ");
            alert.setHeaderText("Приложения транзакции должны обладать параметрами!");
            alert.setContentText("Заполните параметры транзакций!");
            alert.showAndWait();
            return;
        }

        int usedDimInt[] = new int[dataModel.getBasicInfo().D];
        for (int i = 0; i < dataModel.getBasicInfo().D; i++) {
            if (usedDim[i].isSelected()) usedDimInt[i] = 1;
            else usedDimInt[i] = 0;
        }

        int usedUsersInt[] = new int[dataModel.getBasicInfo().U];
        for (int i = 0; i < dataModel.getBasicInfo().U; i++) {
            if (usedUsers[i].isSelected()) usedUsersInt[i] = 1;
            else usedUsersInt[i] = 0;
        }

        int orders[][] = new int[dataModel.getBasicInfo().A][dataModel.getBasicInfo().A];
        for (int i = 0; i < dataModel.getBasicInfo().A; i++) {
            for (int j = 0; j < dataModel.getBasicInfo().A; j++) {
                if (usingOrder[i][j].isSelected()) orders[i][j] = 1;
                else orders[i][j] = 0;
            }
        }

        dataModel.getBasicInfo().transactions[numOfTransaction].a = usedAppInt;
        dataModel.getBasicInfo().transactions[numOfTransaction].d = usedDimInt;
        dataModel.getBasicInfo().transactions[numOfTransaction].u = usedUsersInt;
        dataModel.getBasicInfo().transactions[numOfTransaction].w = orders;
        dataModel.getBasicInfo().transactions[numOfTransaction].paramOfApps = paramOfApps;

        isFilledTrs[numOfTransaction] = true;

        if (isNext) numOfTransaction = (numOfTransaction + 1) % dataModel.getBasicInfo().E;
        else {
            if (numOfTransaction == 0) numOfTransaction = dataModel.getBasicInfo().E - 1;
            else numOfTransaction--;
        }

        paramOfApps = null;
        if (isFilledTrs[numOfTransaction]) {
            usedAppInt = dataModel.getBasicInfo().transactions[numOfTransaction].a;
            usedDimInt = dataModel.getBasicInfo().transactions[numOfTransaction].d;
            usedUsersInt = dataModel.getBasicInfo().transactions[numOfTransaction].u;
            orders = dataModel.getBasicInfo().transactions[numOfTransaction].w;

            for (int i = 0; i < dataModel.getBasicInfo().A; i++)
                usedApp[i].setSelected(usedAppInt[i] == 1);

            for (int i = 0; i < dataModel.getBasicInfo().D; i++)
                usedDim[i].setSelected(usedUsersInt[i] == 1);

            for (int i = 0; i < dataModel.getBasicInfo().U; i++)
                usedUsers[i].setSelected(usedUsersInt[i] == 1);

            for (int i = 0; i < dataModel.getBasicInfo().A; i++) {
                for (int j = 0; j < dataModel.getBasicInfo().A; j++)
                    usingOrder[i][j].setSelected(orders[i][j] == 1);
            }

            myIndexation.setText((numOfTransaction + 1) + " / " + dataModel.getBasicInfo().E);
        } else {
            for (int i = 0; i < dataModel.getBasicInfo().A; i++)
                usedApp[i].setSelected(false);

            for (int i = 0; i < dataModel.getBasicInfo().D; i++)
                usedDim[i].setSelected(false);

            for (int i = 0; i < dataModel.getBasicInfo().U; i++)
                usedUsers[i].setSelected(false);

            for (int i = 0; i < dataModel.getBasicInfo().A; i++) {
                for (int j = 0; j < dataModel.getBasicInfo().A; j++)
                    usingOrder[i][j].setSelected(false);
            }

            myIndexation.setText((numOfTransaction + 1) + " / " + dataModel.getBasicInfo().E);
        }
    }
}
