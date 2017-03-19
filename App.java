package DatabaseChat; /**
 * Created by schueler on 12.03.2017.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private DatabaseChat.Application app;

    @Override
    public void start(Stage stage) {

        app = new DatabaseChat.Application();
        app.setUp();
        stage.setWidth(Utils.WIDTH);
        stage.setHeight(Utils.HEIGHT);
        stage.setScene(new Scene(app));
        stage.show();
    }

}
