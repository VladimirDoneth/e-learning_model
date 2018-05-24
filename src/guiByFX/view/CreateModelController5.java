package guiByFX.view;

import geneticAlgirithmCore.ParamOfApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;

public class CreateModelController5 {
    public static boolean isFiled = false;
    public static int usedAppInt[];
    public static int usedDimInt[];
    public static ParamOfApp [] paramOfApps;
    public static int inxOfTrs;

    private static TextField usedAppsTextFields[];
    private static TextField usedDimTextFields[];

    private static int inxUsedApp;
    private static boolean isNotActivated = true;

    public ScrollPane bAppScrollPane;
    public ScrollPane bDimScrollPane;
    public Label myIndexation;
    public Label myTrsIndexation;

    public void doActivateFields(MouseEvent mouseEvent) {
        if (isNotActivated) {
            isNotActivated = false;
            usedAppsTextFields = new TextField[usedAppInt.length];
            usedDimTextFields = new TextField[usedDimInt.length];

            for (int i = 0; i < usedAppInt.length; i++)
                if (usedAppInt[i] == 1) {
                    inxUsedApp = i;
                    break;
                }

            if (!isFiled) {
                paramOfApps = new ParamOfApp[usedAppInt.length];
            }

            GridPane appGridPane = new GridPane();
            GridPane dimGridPane = new GridPane();

            for (int i = 0; i < usedAppInt.length; i++) {
                Label label = new Label();
                label.setText((i + 1) + "");
                label.setPrefSize(55, 12);

                TextField textField = new TextField();
                if (isFiled) {
                    textField.setText(paramOfApps[inxUsedApp].exchangeBetweenApp[i] + "");
                }
                textField.setPrefSize(55, 12);

                appGridPane.add(label, i, 0);
                appGridPane.add(textField, i, 1);

                usedAppsTextFields[i] = textField;
            }

            for (int i = 0; i < usedDimInt.length; i++) {
                Label label = new Label();
                label.setText((i + 1) + "");
                label.setPrefSize(55, 12);

                TextField textField = new TextField();
                if (isFiled) {
                    textField.setText(paramOfApps[inxUsedApp].exchangeBetweenStorage[i] + "");
                }
                textField.setPrefSize(55, 12);
                dimGridPane.add(label, i, 0);
                dimGridPane.add(textField, i, 1);

                usedDimTextFields[i] = textField;
            }

            bAppScrollPane.setContent(appGridPane);
            bDimScrollPane.setContent(dimGridPane);

            myIndexation.setText((inxUsedApp + 1) + " / " + usedAppInt.length);
            myTrsIndexation.setText("ДЛЯ " + (inxOfTrs + 1) + " ТРАНЗАКЦИИ ");
        }
    }

    public void doNextApp(MouseEvent mouseEvent) {
        doStep(true);
    }

    public void doPerviousApp(MouseEvent mouseEvent) {
        doStep(false);
    }

    public void doCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ВНИМАНИЕ");
        alert.setHeaderText("Вы действительно хотите завершить?");
        alert.setContentText("Добавляемые изменения не сохранятся!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            isFiled = false;
            usedAppInt = null;
            usedDimInt = null;
            paramOfApps = null;
            isNotActivated = true;

            Stage mainStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            mainStage.close();
        }
    }

    public void doNextTask(ActionEvent actionEvent) {
        boolean isHaveError = false;
        for (int i = 0;  i < usedAppInt.length; i++) {
            if (usedAppInt[i] == 1 && paramOfApps[i] == null) isHaveError = true;
        }

        if (isHaveError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА");
            alert.setHeaderText("Ошибка в вводе");
            alert.setContentText("Нарушены ограничения по вводу");
            alert.showAndWait();
        } else {
            CreateModelController.paramOfApps = paramOfApps;
            isFiled = false;
            usedAppInt = null;
            usedDimInt = null;
            paramOfApps = null;
            isNotActivated = true;

            Stage mainStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            mainStage.close();
        }
    }

    private int stepNext() {
        int next = inxUsedApp;
        while (true) {
            next = (next + 1) % usedAppInt.length;
            if (usedAppInt[next] == 1) return next;
        }
    }

    private int stepPrvs() {
        int next = inxUsedApp;
        while (true) {
            if (next == 0) next = usedAppInt.length - 1;
            else next--;
            if (usedAppInt[next] == 1) return next;
        }
    }

    private void doStep(boolean isNext) {
        int [] parA = new int[usedAppInt.length];
        int [] parD = new int[usedDimInt.length];

        try {
            for (int i = 0; i < parA.length; i++)
                parA[i] = Integer.parseInt(usedAppsTextFields[i].getText());
            for (int i = 0; i < parD.length; i++)
                parD[i] = Integer.parseInt(usedDimTextFields[i].getText());
        } catch (Exception hzException) {
            hzException.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА");
            alert.setHeaderText("Ошибка в вводе");
            alert.setContentText("Данные должны быть числа не меншье 0,\nпредаставлющие собой разберность обмениваемой информации");

            alert.showAndWait();
            return;
        }

        boolean isHaveError = false;
        for (int i = 0; i < parA.length; i++) {
            if (usedAppInt[i] == 0 && parA[i] != 0) isHaveError = true;
            System.out.println(usedAppInt[i] + " lala");
        }

        for (int i = 0; i < parD.length; i++) {
            if (usedDimInt[i] == 0 && parD[i] != 0) isHaveError = true;
        }

        if(isHaveError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ОШИБКА");
            alert.setHeaderText("Ошибка в вводе");
            alert.setContentText("Нарушены ограничения по вводу");

            alert.showAndWait();
            return;
        }

        if (paramOfApps[inxUsedApp] == null) paramOfApps[inxUsedApp] = new ParamOfApp();
        paramOfApps[inxUsedApp].exchangeBetweenStorage = parD;
        paramOfApps[inxUsedApp].exchangeBetweenApp = parA;

        if (isNext) inxUsedApp = stepNext();
        else inxUsedApp = stepPrvs();


        for (int i = 0; i < usedAppInt.length; i++) {
            if (isFiled || paramOfApps[inxUsedApp] != null) {
                usedAppsTextFields[i].setText(paramOfApps[inxUsedApp].exchangeBetweenApp[i] + "");
            } else
                usedAppsTextFields[i].setText("");
        }

        for (int i = 0; i < usedDimInt.length; i++) {
            if (isFiled || paramOfApps[inxUsedApp] != null) {
                usedDimTextFields[i].setText(paramOfApps[inxUsedApp].exchangeBetweenStorage[i] + "");
            } else
                usedDimTextFields[i].setText("");
        }
        myIndexation.setText((inxUsedApp + 1) + " / " + usedAppInt.length);
    }
}
