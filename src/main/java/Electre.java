import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Electre extends Application {
    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
      // final MainStage firststage = new MainStage();
       final Parent root = InflateUtils.loadFxml("/fxml/MainActivity.fxml");
       final Scene scene = new Scene(root, 600,600);
       scene.setUserData(this);
       stage.setScene(scene);
       stage.setTitle("Method ELECTRE");
       stage.setHeight(600);
       stage.setWidth(800);
       stage.show();
    }
}
