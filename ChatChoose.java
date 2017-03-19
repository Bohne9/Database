package DatabaseChat;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Created by jonahschueller on 19.03.17.
 */
public class ChatChoose extends TransparentScrollPane{

    private Stage stage;
    private VBox content;


    public ChatChoose(Event e, String users){
        Platform.runLater(()->{
            stage = new Stage();
            content = new VBox();
            content.setMaxHeight(Utils.HEIGHT * 0.5);

            String[] user = users.split(":");

            for (String s : user) {
                String[] info = s.split(",", 2);
                Item item = new Item(Integer.parseInt(info[0]), info[1]);
                item.setOnMouseClicked(event -> {
                    e.onEvent(item.getString());
                    stage.close();
                });
                content.getChildren().add(item);
            }
            setContent(content);
            stage.setScene(new Scene(this));
            stage.show();
        });
    }


    private static class Item extends Label {

        private static Font font = new Font("Courier New", (int)(Utils.HEIGHT * 0.025));
        private int id;
        private String string;

        public Item(int id, String msg){
            super(msg);
            string = id + "," + msg;
            setTextFill(Color.WHITE);
            setFont(font);
            setTextAlignment(TextAlignment.CENTER);
            setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50), null, null)));
            setPrefSize(Utils.WIDTH * 0.2, Utils.HEIGHT * 0.05);
        }

        public String getString() {
            return string;
        }

        public int getID() {
            return id;
        }

        public void setID(int id) {
            this.id = id;
        }

    }


    public interface Event {
        void onEvent(String s);
    }

}
