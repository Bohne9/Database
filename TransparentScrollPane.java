package DatabaseChat;

import javafx.scene.control.ScrollPane;

/**
 * Created by jonahschueller on 17.03.17.
 */
public class TransparentScrollPane extends ScrollPane {

    public TransparentScrollPane() {

        getStylesheets().add(getClass().getResource("scrollpane.css").toExternalForm());
        setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    }
}
