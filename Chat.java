import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by jonahschueller on 17.03.17.
 */
public class Chat extends StackPane{

    private static Font font = new Font("Courier New", (int)(Utils.HEIGHT * 0.05));

    private Circle newMsg;
    private boolean hasNewMsg;
    private ChatPane chatPane;
    private Rectangle ground;
    private Text text;
    private Profile profile;
    private static Color background = Color.rgb(100, 100, 100);

    public Chat(Profile profile){
        this.profile = profile;
        chatPane = new ChatPane(profile);

        text = new Text(profile.getName());
        text.setFont(font);
        text.setWrappingWidth(Utils.WIDTH * 0.3);

        Bounds b = text.getBoundsInLocal();

        ground = new Rectangle(b.getWidth() * 1.01, b.getHeight() * 1.01, background);
        ground.setArcWidth(8);
        ground.setArcHeight(8);

        newMsg = new Circle(b.getHeight() * 0.1, Color.BLUE.brighter());
        newMsg.setTranslateX(b.getWidth() * 0.4);
        hasNewMsg = true;
        setPrefSize(ground.getWidth(), ground.getHeight());
        getChildren().addAll(ground, text);

        setOnMouseClicked(event -> {
            if (hasNewMsg) {
                getChildren().remove(newMsg);
                hasNewMsg = false;
            }
            App.setChatPane(chatPane);
        });

    }

    public void updateNewMsg(){
        if (hasNewMsg){
            getChildren().add(newMsg);
        }
    }

    public ChatPane getChatPane() {
        return chatPane;
    }

    public Profile getProfile() {
        return profile;
    }

    public void addMessage(int type, String msg){
        chatPane.getTimeline().addMessage(type, msg);
        updateNewMsg();
    }
}
