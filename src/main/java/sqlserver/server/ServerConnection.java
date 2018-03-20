package sqlserver.server;

import data.UserData;
import sqlserver.MyConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerConnection extends MyConnection {

    public ServerConnection(String database, String login, String password) {
        super(database, login, password);
    }

    public UserData getUserDB(String login, String password) throws SQLException {
        UserData userData = null;
        PreparedStatement preparedStatement = getConnection().prepareStatement("{call dbo.sp_Authorization(?, ?)}");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if (resultSet.getFetchSize() != 0) {
            userData = new UserData(
                    resultSet.getInt(1),
                    resultSet.getBoolean(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
        }
        return userData;
    }
}