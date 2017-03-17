import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by jonahschueller on 17.03.17.
 */
public class ChatOverview extends TransparentScrollPane{

    private VBox chats;
    private ArrayList<Chat> allChats;

    public ChatOverview() {
        chats = new VBox(Utils.HEIGHT * 0.0005);

        allChats = new ArrayList<>();
        setContent(chats);
        setPrefSize(Utils.WIDTH * 0.3, Utils.HEIGHT * 0.9);
        setMaxSize(Utils.WIDTH * 0.3, Utils.HEIGHT * 0.9);
    }

    public void addChat(Profile profile){
        Chat chat = new Chat(profile);
        chats.getChildren().add(chat);
        allChats.add(chat);
    }


    public ArrayList<Chat> getAllChats() {
        return allChats;
    }

    public Chat getById(int id){
        for (Chat chat : allChats) {
            if (chat.getProfile().getId() == id){
                return chat;
            }
        }
        return null;
    }
}
