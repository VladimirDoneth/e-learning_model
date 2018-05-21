package guiByFX.view;

import guiByFX.model.DataModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

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
        double intensity[][] = new double[CreateModelController.dataModel.getBasicInfo().E][CreateModelController.dataModel.getBasicInfo().U];

        try {
            for (int i = 0; i < CreateModelController.dataModel.getBasicInfo().E; i++) {
                for (int j = 0; j < CreateModelController.dataModel.getBasicInfo().U; j++) {
                    intensity[i][j] = Double.parseDouble(intensityText[j][i].getText());
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

            intensityText = new TextField[CreateModelController.dataModel.getBasicInfo().E][CreateModelController.dataModel.getBasicInfo().U];
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
        System.out.println("it is work");
        if (isNotMade) {
            isNotMade = false;

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
                gridPaneUsers.add(usedDim[i], i, 1);
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
    }

    public void doPerviousTrs(MouseEvent mouseEvent) {
    }

    public void doFillParamApp(MouseEvent mouseEvent) {
    }

    public void doSave(ActionEvent actionEvent) {
    }
}
