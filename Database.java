import java.sql.*;
import java.util.ArrayList;

/**
 * Created by schueler on 12.03.2017.
 */
public class Database {

    private static Connection chats;

    static {
        startup();
    }

    public static Connection getChats() {
        return chats;
    }

    public static void startup(){
       startup(1);
       create();
    }

    private static void startup(int i){
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

}
