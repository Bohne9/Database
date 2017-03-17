import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

/**
 * Created by jonahschueller on 17.03.17.
 */
public class ChatPane extends VBox{


    private TimelinePane timeline;
    private TextField input;
    private ChatHeader header;
    private Profile profile;

    public ChatPane(Profile profile) {
        this.profile = profile;

        timeline = new TimelinePane();

        input = new TextField();
        input.setPrefWidth(Utils.WIDTH * .7);
        input.setMaxWidth(Utils.WIDTH * .7);

        input.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)){
                getTimeline().addMessage(TextMessage.ME, input.getText());
                input.clear();
            }
        });

        header = new ChatHeader(profile.getName(), Utils.HEIGHT * 0.3 - input.getBoundsInLocal().getHeight());
        getChildren().addAll(header, timeline, input);
    }

    public TimelinePane getTimeline() {
        return timeline;
    }
}
