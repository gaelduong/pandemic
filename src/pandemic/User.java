package pandemic;

import java.io.Serializable;

public class User implements Serializable  {
    private String userName;
    private String userPassword;

    public void setUserIPAddress(String userIPAddress) {
        this.userIPAddress = userIPAddress;
    }

    private String userIPAddress;

    public User(String userName, String userPassword, String userIPAddress) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userIPAddress = userIPAddress;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserIPAddress() {
        return userIPAddress;
    }
}
