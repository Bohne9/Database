package DatabaseChat;

import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Created by jonahschueller on 17.03.17.
 */
public class TextMessage extends StackPane{

    private static Font font = new Font("Courier New", (int)(Utils.HEIGHT * 0.02));
    private Text text;
    private Rectangle ground;

    private static Color my = Color.rgb(240, 240, 240), other = Color.rgb(200, 255, 200);

    public static int ME = 0, OTHER = 1;
    public TextMessage(int type, String msg) {
        text = new Text(msg);
        text.setFont(font);

        text.setFill(Color.DARKGRAY.darker());
        text.setWrappingWidth(Utils.WIDTH * 0.2);

        Bounds b = text.getBoundsInLocal();

        ground = new Rectangle(b.getWidth() * 1.05, b.getHeight() * 1.05, type == ME ? my : type == OTHER ? other : my);
        ground.setArcWidth(10);
        ground.setArcHeight(10);

        setPrefSize(ground.getWidth(), ground.getHeight());
        getChildren().addAll(ground, text);

        switch (type){
            case 0:
                setTranslateX(Utils.WIDTH * 0.35 - b.getWidth());
                break;
            case 1:
                setTranslateX(-Utils.WIDTH * 0.35 + b.getWidth());
                break;
        }

    }
}
