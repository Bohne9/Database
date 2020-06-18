package DatabaseChat;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jonahschueller on 17.03.17.
 */
public class ChatOverview extends VBox{

    private VBox chats;
    private ArrayList<Chat> allChats;
    private NewChat newChat;
    private TransparentScrollPane scroll;

    public ChatOverview() {
        chats = new VBox(Utils.HEIGHT * 0.0005);
        scroll = new TransparentScrollPane();
        allChats = new ArrayList<>();
        scroll.setContent(chats);
        scroll.setPrefSize(Utils.WIDTH * 0.3, Utils.HEIGHT * 0.9);
        scroll.setMaxSize(Utils.WIDTH * 0.3, Utils.HEIGHT * 0.9);

        newChat = new NewChat();

        newChat.setOnMouseClicked(event -> {
            String content = "" + Profile.getMe().getId();
            try {
                NetworkEnviroment.getUser().write("getallusers:" + content.getBytes().length, content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        getChildren().addAll(newChat, scroll);

    }

    public void addChat(int id, Profile profile){
        Chat chat = new Chat(id, profile);
        Platform.runLater(()->{
            chats.getChildren().add(chat);
        });

        allChats.add(chat);
    }


    public NewChat getNewChat() {
        return newChat;
    }

    public ArrayList<Chat> getAllChats() {
        return allChats;
    }

    public Chat getById(int id){
        for (Chat chat : allChats) {
            if (chat.getChatID() == id){
                return chat;
            }
        }
        return null;
    }


    public boolean hasChat(int id){
        for (Chat chat : allChats) {
            if (chat.getChatID() == id)
                return true;
        }
        return false;
    }


    private class NewChat extends StackPane{
        private Rectangle ground;
        private Text  text;

        public NewChat(){

            text = new Text("+ Neuer Chat");
            text.setTextAlignment(TextAlignment.CENTER);
            text.setFont(new Font("Courier New", (int)(Utils.HEIGHT * 0.04)));
            text.setWrappingWidth(Utils.WIDTH * 0.3);

            Bounds b = text.getBoundsInLocal();

            ground = new Rectangle(b.getWidth() * 1.01, b.getHeight() * 1.01, Color.rgb(100, 100, 100));
            ground.setArcWidth(8);
            ground.setArcHeight(8);

            setOnMouseEntered(event -> setOpacity(0.8));
            setOnMouseExited(event -> setOpacity(1));
            setPrefSize(ground.getWidth(), ground.getHeight());
            getChildren().addAll(ground, text);
        }



    }
}
