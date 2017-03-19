package DatabaseServer;

import java.util.ArrayList;

/**
 * Created by jonahschueller on 18.03.17.
 */
public class ID {


    private static int databaseUserID;

    private static ArrayList<Integer> ids = new ArrayList<>();

    public static void setDatabaseUserID(int databaseUserID) {
        ID.databaseUserID = databaseUserID;
    }

    public static int nextDatabaseUserID() {
        databaseUserID += 1;
        ids.add(new Integer(databaseUserID));
        return databaseUserID;
    }

    public static void remove(int id){
        ids.remove(new Integer(id));
    }
}
