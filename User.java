package DatabaseServer;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jonahschueller on 18.03.17.
 */
public class User{

    private Socket socket;
    private int id;

    public User(Socket socket){
        this.socket = socket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void write(String node, String content) throws IOException {
        socket.getOutputStream().write(node.getBytes());
        socket.getOutputStream().write(-1);
        socket.getOutputStream().write(content.getBytes());
        socket.getOutputStream().write(-1);
    }
}
