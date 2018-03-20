package sqlserver;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnection {
    Connection getConnection();

    void connect() throws SQLException;

    boolean isConnected();

    void disconnect();
}
