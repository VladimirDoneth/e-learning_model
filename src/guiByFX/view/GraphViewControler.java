package guiByFX.view;

import geneticAlgirithmCore.BasicInfo;
import guiByFX.model.ResultModel;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GraphViewControler {
    public static ResultModel resultModel;
    private static final int FIT_SIZE = 50;

    public ScrollPane mScrollPane;

    @FXML
    private void initialize() throws FileNotFoundException {
        GridPane gridPane = new GridPane();
        int [][] mG = resultModel.getAgent().getmG();
        int [][] mS = resultModel.getAgent().getmS();
        BasicInfo basicInfo = resultModel.getAgent().getBasicInfo();

        int lever[] = new int[basicInfo.N];
        Image app = new Image(new FileInputStream("src/data/app.png"));
        Image server = new Image(new FileInputStream("src/data/server.png"));
        Image storage = new Image(new FileInputStream("src/data/storage.png"));
        Image user = new Image (new FileInputStream("src/data/user.png"));

        for (int i = 0; i < basicInfo.N; i++) {
            lever[i] = 0;
            ImageView view = new ImageView(server);
            view.setFitHeight(FIT_SIZE);
            view.setFitWidth(FIT_SIZE);
            gridPane.add(view, i, lever[i]);
            lever[i] = 1;
        }

        int usr = basicInfo.U;

        while (usr != 0) {
            ImageView view = new ImageView(user);
            view.setFitHeight(FIT_SIZE);
            view.setFitWidth(FIT_SIZE);
            int inx = (int)(Math.random() * basicInfo.N);
            gridPane.add(view, inx, lever[inx]);
            lever[inx]++;
            usr--;
        }

        int maxLever = 0;
        for (int i = 0; i < lever.length; i++)
            if (maxLever < lever[i]) maxLever = lever [i];

        for (int i = 0; i < lever.length; i++)
            lever[i] = maxLever + 1;

        for (int i = 0; i < basicInfo.A; i++){
            for (int j = 0; j < basicInfo.N; j++) {
                if (mG[i][j] == 1) {
                    ImageView view = new ImageView(app);
                    view.setFitHeight(FIT_SIZE);
                    view.setFitWidth(FIT_SIZE);
                    gridPane.add(view, j, lever[j]);
                    lever[j]++;
                }
            }
        }

        maxLever = 0;
        for (int i = 0; i < lever.length; i++)
            if (maxLever < lever[i]) maxLever = lever [i];

        for (int i = 0; i < lever.length; i++)
            lever[i] = maxLever + 1;

        for (int i = 0; i < basicInfo.D; i++){
            for (int j = 0; j < basicInfo.N; j++) {
                if (mS[i][j] == 1) {
                    ImageView view = new ImageView(storage);
                    view.setFitHeight(FIT_SIZE);
                    view.setFitWidth(FIT_SIZE);
                    gridPane.add(view, j, lever[j]);
                    lever[j]++;
                }
            }
        }

        mScrollPane.setContent(gridPane);
    }
}
