package pandemic;

public class User {
    private String userName;
    private String userPassword;
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
