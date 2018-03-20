package sqlserver;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.sql.Connection;
import java.sql.SQLException;
import static data.ConnectConstants.ADDRESS_CONNECTION;

public class MyConnection implements IConnection{

    private Connection connection = null;
    private String connectionString = null;

    public MyConnection(String database, String login, String password) {
        connectionString = ADDRESS_CONNECTION + ";"
                + "database=" + database + ";"
                + "user=" + login + ";"
                + "password=" + password + ";"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;"
                + "loginTimeout=30;";
    }

    public Connection getConnection() {
        return connection;
    }

    public void connect() throws SQLException {
        try {
            SQLServerDriver driver = new SQLServerDriver();
            connection = driver.connect(connectionString, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connection != null ? true : false;
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}