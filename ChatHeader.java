import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by jonahschueller on 17.03.17.
 */
public class ChatHeader extends StackPane {

    private static Font font = new Font("Courier New", Utils.HEIGHT * 0.03);
    private Text text;
    private Rectangle ground;

    public ChatHeader(String msg, double h){

        text = new Text(msg);
        text.setWrappingWidth(Utils.WIDTH * 0.7);
        text.setFont(font);
        text.setFill(Color.BLACK);

        ground = new Rectangle(Utils.WIDTH * 0.7, Utils.HEIGHT * 0.03, Color.rgb(70, 70, 70));
        ground.setArcWidth(4);
        ground.setArcHeight(4);

        setMaxSize(Utils.WIDTH * 0.7, h);
        getChildren().addAll(ground, text);
    }

}
