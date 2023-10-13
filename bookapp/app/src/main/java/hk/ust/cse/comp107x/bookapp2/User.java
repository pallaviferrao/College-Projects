package hk.ust.cse.comp107x.bookapp2;

/**
 * Created by dell on 6/29/2016.
 */

/**
 * Defines the data structure for User objects.
 */
public class User {
    private String userName;
    private String email;
    // private boolean hasLoggedInWithPassword;


    /**
     * Required public constructor
     */
    public User() {

    }

    /**
     * Use this constructor to create new User.
     * Takes user name, email and timestampJoined as params
     *
     * @param name
     * @param email
     *
     */
    public User(String name, String email) {
        this.userName = name;
        this.email = email;

        //  this.hasLoggedInWithPassword = false;
    }

    public String getName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }


    /*public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }*/
}
