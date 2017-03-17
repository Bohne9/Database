import java.io.IOException;
import java.net.Socket;

/**
 * Created by schueler on 12.03.2017.
 */

public class User implements Runnable{

    private int id;
    private String name;
    private String email;
    private byte[] hash;
    private Socket connection;
    private Thread receive;
    private boolean running = true;
    private Receive re;

    public User(int id, String name, String email, byte[] hash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.hash = hash;
    }

    public void connect(String ip, int port){
        try {
            connection = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        receive = new Thread(this);
        receive.start();
    }

    public void sendMsg(int id, String text) throws IOException{
        connection.getOutputStream().write(this.id);
        connection.getOutputStream().write(-1);
        connection.getOutputStream().write(id);
        connection.getOutputStream().write(-1);
        connection.getOutputStream().write(text.getBytes());
        connection.getOutputStream().write(-1);
    }

    @Override
    public void run() {
        while (running){
            StringBuilder string = new StringBuilder();
            int i = 0;
            for (int j = 0; j < 3; j++) {
                try {
                    while ((i = (byte)connection.getInputStream().read()) != -1){
                        string.append((char)i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            re.execute(string.toString());
        }
    }
}
