package data;

public class UserData {

    private static UserData userData = null;
    private int idAccount = 0;
    private boolean admin = false;
    private String fullName = null;
    private String dbAccountLogin = null;
    private String dbAccountPassword = null;

    public static void setUserData(UserData data) {
        userData = data;
    }

    public static UserData getUserData() {
        return userData;
    }

    public UserData(int idAccount, boolean admin, String fullName, String dbAccountLogin, String dbAccountPassword) {
        this.idAccount = idAccount;
        this.admin = admin;
        this.fullName = fullName;
        this.dbAccountLogin = dbAccountLogin;
        this.dbAccountPassword = dbAccountPassword;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDbAccountLogin() {
        return dbAccountLogin;
    }

    public String getDbAccountPassword() {
        return dbAccountPassword;
    }
}