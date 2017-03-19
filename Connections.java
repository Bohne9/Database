package DatabaseServer;

import DatabaseChat.CommunicationTree;
import DatabaseChat.NetworkEnviroment;

import java.io.IOException;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import static DatabaseChat.CommunicationTree.*;

/**
 * Created by jonahschueller on 18.03.17.
 */
public class Connections {

    private ServerSocket server;
    private ArrayList<User> clients;
    private DatagramSocket broadcast;
    private boolean broadcasting, receiving;
    private Thread b_th, s_th;
    private static CommunicationTree communication;

    public Connections(){
        clients = new ArrayList<>();
        try {
            server = new ServerSocket(1337);
            broadcast = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        communication = new CommunicationTree();
        setUpCommunicationTree();

    }


    public void start(){
        communication.start();
        startBroadcast();
        startServer();
    }



    public void startBroadcast(){
        b_th = new Thread(){
            @Override
            public void run() {
                InetAddress host = getBroadcast().get(0);
                broadcasting = true;
                while (broadcasting){

                    byte[] key = NetworkEnviroment.B_KEY.getBytes();
                    DatagramPacket packet = new DatagramPacket(key, key.length, host, NetworkEnviroment.B_PORT);
                    try {
                        broadcast.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        b_th.start();
    }


    public void startServer(){
        s_th = new Thread(){
            @Override
            public void run() {
                receiving = true;
                while (receiving){
                    try {
                        Socket socket = server.accept();
                        User user = new User(socket);
                        clients.add(user);
                        communication.addConnection(socket);
                        int id = ID.nextDatabaseUserID();
                        user.setId(id);
                        String content = id + ":";
                        String output = "start:" + content.getBytes().length;
                        user.write(output, content);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        s_th.start();
    }



    public void setUpCommunicationTree(){

        Node client = createNode("client", (bytes)->{
            String[] parts = new String(bytes).split(":", 3);
            int id = Integer.parseInt(parts[0]);
            int sid = Integer.parseInt(parts[1]);
            if (id == 0){
                DatabaseServer.addUser(sid, parts[2]);
            }else {
                DatabaseServer.executeUpdate("UPDATE User set Online = 'True' WHERE ID = " + id);
                User user = getUserByID(sid);
                user.setId(id);
                ID.remove(sid);
            }
        });


        Node allUsers = createNode("getallusers", (bytes)->{
            String[] parts = new String(bytes).split(":");
            int id = Integer.parseInt(parts[0]);
            User user = getUserByID(id);

            String users = DatabaseServer.getAllUsers();

            String output = "allusers:" + users.getBytes().length;

            try {
                user.write(output, users);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        Node newchat = createNode("newchat", (bytes)->{
            String[] user = new String(bytes).split(":", 3);

            int chatid = DatabaseServer.addChat(Integer.parseInt(user[0]), Integer.parseInt(user[1]));

            String content = chatid + ":" + user[1] + ":" + user[2];

            User u = getUserByID(Integer.parseInt(user[0]));

            try {
                u.write("chat:" + content.getBytes().length, content);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        Node msg = createNode("msg", (bytes)->{
            System.out.println("MSG");
            //1. chatid - 2. senderid - 3. text
            String[] parts = new String(bytes).split(":", 3);
            int chatid = Integer.parseInt(parts[0]);
            int senderid = Integer.parseInt(parts[1]);
            String chat = DatabaseServer.getChat(chatid);

            DatabaseServer.addMsg(chatid, senderid, parts[2]);

            String sql = "SELECT ID, Online FROM User WHERE ID in (SELECT User1ID FROM Chats WHERE ID = " + chatid + ") OR ID in (SELECT User2ID FROM Chats WHERE ID = " + chatid + ")";
            ResultSet result = DatabaseServer.executeQuery(sql);
            boolean online = false;
            try {
                online = result.getBoolean("Online") && result.getInt("ID") != senderid;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (online){
                User user = null;
                try {
                    user = getUserByID(result.getInt("ID"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String content = chatid + ":" + parts[2];
                try {
                    user.write("msg:" + content.getBytes().length, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });


    }


    public User getUserByID(int id){
        for (User user :
                clients) {
            if (user.getId() == id){
                return user;
            }
        }
        try {
            throw new Exception("ID " + id + " not found in clients list.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<InetAddress> getBroadcast() {
        ArrayList<InetAddress> broadcast = new ArrayList<>();

        //Wird ausgeführt, wenn des erste Mal auf die Broadcast-Adressen zugegriffen wird.
        try {
            Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces();
            while (networkInterface.hasMoreElements()){
                NetworkInterface inter = networkInterface.nextElement();
                List<InterfaceAddress> addresses = inter.getInterfaceAddresses();
                for (InterfaceAddress interfaceAddress : addresses){
                    InetAddress broad = interfaceAddress.getBroadcast();
                    if (broad != null){
                        broadcast.add(broad);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return broadcast;

    }

}
