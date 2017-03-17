import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by jonahschueller on 17.03.17.
 */
public class TimelinePane extends TransparentScrollPane {

    private VBox timeline;

    public TimelinePane() {
        timeline = new VBox(Utils.HEIGHT * 0.001, new Rectangle(1, Utils.HEIGHT * 0.025, Color.TRANSPARENT));
        timeline.setPrefSize(Utils.WIDTH * 0.7, Utils.HEIGHT * 0.9);
        timeline.setBackground(new Background(new BackgroundFill(Color.rgb(51, 51, 51), null, null)));
        setPrefSize(Utils.WIDTH * 0.7, Utils.HEIGHT * 0.9);
        setMaxSize(Utils.WIDTH * 0.7, Utils.HEIGHT * 0.9);
        setContent(timeline);
    }

    public void addMessage(int type, String msg){
        TextMessage tmsg = new TextMessage(type, msg);
        timeline.getChildren().add(tmsg);
        layout();
        if (timeline.getHeight() > Utils.HEIGHT * 0.8)
            setVvalue(1.0);
    }

}