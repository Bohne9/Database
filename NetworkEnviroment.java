package DatabaseChat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by jonahschueller on 17.03.17.
 */
public class NetworkEnviroment {

    public static int B_PORT = 8192;
    public static int S_PORT = 1337;
    public static String B_KEY = "CHAT_REGI";

    private static User user= new User(Profile.getMe().getName());
    private static CommunicationTree communication;
    private DatagramSocket broadcast;


    public NetworkEnviroment(){
        communication = new CommunicationTree();
    }


    public static CommunicationTree getCommunication() {
        return communication;
    }

    public void startUp() throws IOException {

        broadcast = new DatagramSocket(B_PORT);
        broadcast.setSoTimeout(2000);
        for (int i = 0; i < 10; i++) {
            byte[] buf = new byte[B_KEY.length()];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                broadcast.receive(packet);
                String msg = new String(packet.getData());
                if (msg.equals(B_KEY)) {
                    user.connect(packet.getAddress().getHostAddress(), S_PORT);
                    System.out.println("CONNECTED");
                    communication.addConnection(user.getConnection());
                    communication.start();
                    break;
                }
        }


    }


    public static void addNode(CommunicationTree.Node node){
        communication.addNode(node);
    }


    public static User getUser() {
        return user;
    }
}
