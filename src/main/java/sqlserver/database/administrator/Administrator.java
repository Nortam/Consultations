package sqlserver.database.administrator;

import sqlserver.database.consultant.Consultant;

public class Administrator extends Consultant {

    private static Administrator administrator = null;

    public static Administrator getAdministrator() {
        return administrator;
    }

    public static void setAdministrator(Administrator administrator) {
        Administrator.administrator = administrator;
    }

    public Administrator(String database, String login, String password) {
        super(database, login, password);
    }
}