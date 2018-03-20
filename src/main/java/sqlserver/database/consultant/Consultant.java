package sqlserver.database.consultant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sqlserver.MyConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Consultant extends MyConnection {

    private static Consultant consultant = null;

    public static Consultant getConsultant() {
        return consultant;
    }

    public static void setConsultant(Consultant consultant) {
        Consultant.consultant = consultant;
    }

    public Consultant(String database, String login, String password) {
        super(database, login, password);
    }

    public void changePassword(String newPassword) {
    	//sp_AcountApp_Change
    }

    public ResultSet getTableData(String date) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("{call dbo.sp_ConsCalendarWeek(?)}");
        preparedStatement.setString(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getConsultationTypes(Consultant user) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("SELECT constypes_id, constypes FROM dbo.tab_ConsultationTypes");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getConsultationType(Consultant user, int id) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("SELECT constypes_id, constypes FROM dbo.tab_ConsultationTypes WHERE constypes_id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getLocations(Consultant user) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("SELECT location_id, location FROM dbo.tab_Location");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getLocation(Consultant user, int id) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("SELECT location_id, location FROM dbo.tab_Location WHERE location_id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getPlacesStudy(Consultant user) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("SELECT placestudy_id, placestudy FROM dbo.tab_PlaceStudy");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getPlaceStudy(Consultant user, int id) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("SELECT placestudy_id, placestudy FROM dbo.tab_PlaceStudy WHERE placestudy_id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getPlacesWork(Consultant user) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("SELECT placework_id, placework FROM dbo.tab_PlaceWork");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getPlaceWork(Consultant user, int id) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("SELECT placework_id, placework FROM dbo.tab_PlaceWork WHERE placework_id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getConsultants(Consultant user) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("{call dbo.sp_Consultants()}");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public ResultSet getConsultation(Consultant user, int id) throws SQLException {
        PreparedStatement preparedStatement = user.getConnection().prepareStatement("SELECT * FROM dbo.tab_Consultation WHERE cons_id=?;");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getFetchSize() != 0) {
            return resultSet;
        }
        return null;
    }

    public boolean pickUpConsultation() {
        Statement stmt = null;

        return false;
    }
}