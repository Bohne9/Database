package DatabaseChat;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by schueler on 12.03.2017.
 */
public class Database {

    private static Connection chats;

    public static Connection getChats() {
        return chats;
    }

    public static void startup(){
       startup(1);
       create();
       insertUtils();
    }

    public static void startup(int i){
        try {
            Class.forName("org.sqlite.JDBC");
            chats = DriverManager.getConnection("jdbc:sqlite:chatid.db");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static void create(){
        try {
            Statement st = chats.createStatement();
            String create = "CREATE TABLE IF NOT EXISTS Chats (ID INT PRIMARY KEY NOT NULL)";
            st.executeUpdate(create);

            Statement profile = chats.createStatement();
            String sqlProfile = "CREATE TABLE IF NOT EXISTS Me (ID INT PRIMARY KEY NOT NULL, Name VARCHAR(255))";
            profile.executeUpdate(sqlProfile);

            chats.close();
            startup(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addChat(int id)  {
        String insert = "INSERT INTO Chats VALUES(" + id + ")";
        try {
            Statement stmt = chats.createStatement();
            stmt.executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setID(int id){
        if (!hasNameEntry()){
            try {
                Statement setid = chats.createStatement();
                String sql = "UPDATE Me set ID = " + id + " WHERE ID = 0";
                setid.executeUpdate(sql);

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void setName(String name){
        if (!hasNameEntry()){
            try {
                Statement setname = chats.createStatement();
                String sql = "UPDATE Me set Name = " + name + " WHERE ID = 0";
                setname.executeUpdate(sql);

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Integer> getChatIDs(){
        String select = "SELECT * FROM Chats";

        Statement get = null;
        ArrayList<Integer> ids = null;
        try {
            ids = new ArrayList<>();
            get = chats.createStatement();
            ResultSet set = get.executeQuery(select);

            while (set.next()){
                int id = set.getInt("id");
                ids.add(new Integer(id));
            }
        } catch (SQLException e) {
        e.printStackTrace();
        }
        return ids;
    }

    public static void insertName(){
        String insert = "INSERT INTO Me (ID, Name) VALUES(" + 0 + ", " + "'Jonah'" + ")";
        try {
            Statement stmt = chats.createStatement();
            stmt.executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasNameEntry(){
        try {
            Statement name = chats.createStatement();
            String sql = "SELECT ID FROM Me";

            ResultSet set = name.executeQuery(sql);

            while(set.next()){
                int id = set.getInt("ID");
                if (id == 0){
                    //insertName();
                    return false;
                }else{
                    return true;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("EROR");
        return false;
    }

    public static void insertUtils(){
        try {
            Statement count = chats.createStatement();
            String sql = "SELECT COUNT(ID) FROM Me";
            ResultSet res = count.executeQuery(sql);

            int size = res.getInt("COUNT(ID)");
            res.close();
            count.close();
            if (size == 0){
                System.out.println("INSERT");
                insertName();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }



    }


    public static void setUserID(){
        try {
            Statement name = chats.createStatement();
            String sql = "SELECT ID FROM Me";

            ResultSet set = name.executeQuery(sql);

            while(set.next()){
                int id = set.getInt("ID");
                System.out.println("ID: " + id);
                NetworkEnviroment.getUser().setId(id);
                Profile.getMe().setId(id);
            }
            set.close();
            name.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static void executeUpdate(String sql){
        try {
            Statement stmt = chats.createStatement();

            stmt.executeUpdate(sql);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static ResultSet executeQuery(String sql){
        try {
            Statement stmt = chats.createStatement();

            return stmt.executeQuery(sql);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
