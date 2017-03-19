package DatabaseServer;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by jonahschueller on 18.03.17.
 */
public class Server extends Application{


    private Connections connections;

    @Override
    public void start(Stage primaryStage) throws Exception {

        DatabaseServer.getConnection();
        connections = new Connections();
        connections.start();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
