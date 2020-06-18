package DatabaseChat;

/**
 * Created by jonahschueller on 17.03.17.
 */
public class Profile {

    private static Profile me = new Profile(0, "Jonah");
    private int id;
    private String name;

    public Profile(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static void setMe(Profile me) {
        Profile.me = me;
    }

    public static Profile getMe() {
        return me;
    }
}
