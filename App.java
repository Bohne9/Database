/**
 * Created by schueler on 12.03.2017.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static HBox content;

    @Override
    public void start(Stage stage) {

        content = new HBox();
        content.setPadding(new Insets(10));
        Profile profile = new Profile(2, "ceafew");

        ChatOverview overview = new ChatOverview();
        content.getChildren().add(overview);

        for (int i = 0; i < 3; i++) {
            overview.addChat(profile);
        }



        Chat chat = overview.getById(2);

        chat.addMessage(TextMessage.OTHER, "Hiii :)");

        stage.setWidth(Utils.WIDTH);
        stage.setHeight(Utils.HEIGHT);
        stage.setScene(new Scene(content));
        stage.show();
    }


    public static void setChatPane(Pane pane){
        if (content.getChildren().size() == 2)
            content.getChildren().remove(1);
        content.getChildren().add(pane);
    }
}
