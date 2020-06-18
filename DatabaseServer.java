package DatabaseServer;

import java.sql.*;

/**
 * Created by jonahschueller on 18.03.17.
 */
public class DatabaseServer {


    private static Connection connection;


    static{
        startUp();
        create();
        setId();
    }


    public static void startUp(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static void create(){

        try {
            Statement profile = connection.createStatement();
            String sqlProfile = "CREATE TABLE IF NOT EXISTS User (ID INT PRIMARY KEY NOT NULL, Name VARCHAR(255), Online BOOLEAN)";
            profile.execute(sqlProfile);

            Statement chats = connection.createStatement();
            String create = "CREATE TABLE IF NOT EXISTS Chats (ID INTEGER PRIMARY KEY AUTOINCREMENT, User1ID INT, User2ID INT, FOREIGN KEY (User1ID) REFERENCES User(ID), FOREIGN KEY (User2ID) REFERENCES User(ID))";
            chats.executeUpdate(create);

            Statement messages = connection.createStatement();
            String sqlMsg = "CREATE TABLE IF NOT EXISTS Messages (ID INTEGER PRIMARY KEY AUTOINCREMENT, ChatID INT, SenderID INT, MSG TEXT, Time INT, " +
                            "FOREIGN KEY (ChatID) REFERENCES Chats(ID), FOREIGN KEY (SenderID) REFERENCES User(ID))";
            messages.executeUpdate(sqlMsg);

            connection.close();
            startUp();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }


    public static void addMsg(int chatid, int senderid, String text){
        String insert = "INSERT INTO Messages (ChatID, SenderID, MSG) VALUES(" + chatid + ", " + senderid + ", '" + text + "')";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(int id, String name)  {
        String insert = "INSERT INTO User (ID, Name, Online) VALUES(" + id + ", '" + name + "', 'True')";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void setId(){
        String select = "SELECT MAX(ID) FROM User";
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(select);
            ID.setDatabaseUserID(result.getInt(1) + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String getChat(int id){
        String select = "SELECT * FROM Chats WHERE ID = " + id;
        StringBuilder chat = new StringBuilder();
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(select);

            chat.append(res.getInt("ID"));
            chat.append(":");
            chat.append(res.getInt("User1ID"));
            chat.append(":");
            chat.append(res.getInt("User2ID"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chat.toString();
    }



    public static int addChat(int u1, int u2){
        String insert = "INSERT INTO Chats (User1ID, User2ID) VALUES(" + u1 + ", " + u2 + ")";
        int id = 0;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(insert);
            stmt.close();

            String getid = "SELECT Max(ID) FROM Chats";
            Statement query = connection.createStatement();
            ResultSet set = query.executeQuery(getid);
            id = set.getInt("Max(ID)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }



    public static String getAllUsers(){

        String select = "SELECT * FROM User";
        StringBuilder users = new StringBuilder();
        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(select);

            while (result.next()){
                users.append(result.getInt("ID"));
                users.append(",");
                users.append(result.getString("Name"));
                users.append(":");
            }
            stmt.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users.toString();
    }

    public static void executeUpdate(String sql){
        try {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate(sql);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static ResultSet executeQuery(String sql){
        try {
            Statement stmt = connection.createStatement();

            return stmt.executeQuery(sql);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
