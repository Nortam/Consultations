package data.сollections;

import java.sql.Date;
import java.sql.Time;

public class DataWeek {

    private int id;
    private String wd;
    private Date date;
    private Time time;
    private String type;
    private String otm;
    private String person;
    private String location;
    private String place;
    private String consultant;

    public DataWeek(int id, String wd, Date date, Time time, String type, String otm, String person, String location, String place, String consultant) {
        this.id = id;
        this.wd = wd;
        this.date = date;
        this.time = time;
        this.type = type;
        this.otm = otm;
        this.person = person;
        this.location = location;
        this.place = place;
        this.consultant = consultant;
    }

    public int getId() {
        return id;
    }

    public String getWd() {
        return wd;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getOtm() {
        return otm;
    }

    public String getPerson() {
        return person;
    }

    public String getLocation() {
        return location;
    }

    public String getPlace() {
        return place;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
