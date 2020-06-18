package DatabaseChat;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import java.io.IOException;
import static DatabaseChat.CommunicationTree.Node;
import static DatabaseChat.CommunicationTree.createNode;

/**
 * Created by jonahschueller on 18.03.17.
 */
public class Application extends Pane{

    private NetworkEnviroment enviroment;
    private ChatOverview overview;
    private static HBox content;
    private static ChatPane current;

    public Application(){
        enviroment = new NetworkEnviroment();
        overview = new ChatOverview();

        content = new HBox();
        content.getChildren().add(overview);
        getChildren().add(content);
    }

    public void setUp(){
        Database.startup();

        setUpTree();
        try {
            enviroment.startUp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setUpTree(){
        CommunicationTree communication = NetworkEnviroment.getCommunication();

        Node idInit = createNode("start", (bytes)->{
            Database.setUserID();
            String[] parts = new String(bytes).split(":");
            int id = Integer.parseInt(parts[0]);
            if (NetworkEnviroment.getUser().getId() == 0){
                NetworkEnviroment.getUser().setId(id);
                Database.setID(id);
                String content = "0:" + id + ":" + Profile.getMe().getName();
                String output = "client:" + content.getBytes().length;
                try {
                    NetworkEnviroment.getUser().write(output, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                String content = NetworkEnviroment.getUser().getId() + ":" + id + ":" + Profile.getMe().getName();
                String output = "client:" + content.getBytes().length;
                try {
                    NetworkEnviroment.getUser().write(output, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        Node allusers = createNode("allusers", (bytes)->{
            String users = new String(bytes);

            ChatChoose.Event e = (s)->{
                String[] info = s.split(",", 2);

                s = Profile.getMe().getId() + ":" + info[0] + ":" + info[1];
                String newchat = "newchat:" + s.getBytes().length;
                try {
                    NetworkEnviroment.getUser().write(newchat, s);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            };

            new ChatChoose(e, users);

        });

        Node chat = createNode("chat", (bytes)->{
            String[] parts = new String(bytes).split(":", 3);
            int id = Integer.parseInt(parts[0]);
            if (!overview.hasChat(id)){
                Profile profile = new Profile(Integer.parseInt(parts[1]), parts[2]);
                overview.addChat(id, profile);
            }
        });

        Node msg = createNode("msg", (bytes)->{
            String[] parts = new String(bytes).split(":", 2);
            int id = Integer.parseInt(parts[0]);
            String text = parts[1];
            System.out.println("CHAT:  "+ id);
            if (overview.hasChat(id)){
                System.out.println("HAS CHAT");
                Chat c = overview.getById(id);
                System.out.println(c == null);
                c.addMessage(TextMessage.OTHER, text);
            }
        });

        Node startMsg = createNode("startMsg", (bytes)->{
            String[] parts = new String(bytes).split(":", 2);
            int id = Integer.parseInt(parts[0]);
            String text = parts[1];
            if (overview.hasChat(id)){
                Chat c = overview.getById(id);
                c.getChatPane().getTimeline().addMessage(TextMessage.OTHER, text);
            }
        });
    }

    public static HBox getContent() {
        return content;
    }

    public static void setChatPane(ChatPane pane){
        current = pane;
        if (content.getChildren().size() == 2)
            content.getChildren().remove(1);
        content.getChildren().add(pane);
    }

    public static ChatPane getCurrent() {
        return current;
    }
}
