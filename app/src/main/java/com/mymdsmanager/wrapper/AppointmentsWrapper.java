package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 9/9/15.
 */
public class AppointmentsWrapper {


    private int arowid = 0;

    public String getDateNtime() {
        return dateNtime;
    }

    public void setDateNtime(String dateNtime) {
        this.dateNtime = dateNtime;
    }

    public int getArowid() {
        return arowid;
    }

    public void setArowid(int arowid) {
        this.arowid = arowid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDatetimestamp() {
        return datetimestamp;
    }

    public void setDatetimestamp(String datetimestamp) {
        this.datetimestamp = datetimestamp;
    }

    private String dateNtime = "";
    private String provider = "";
    private String notes = "";
    private String datetimestamp = "";

    public String getSearch_date() {
        return search_date;
    }

    public void setSearch_date(String search_date) {
        this.search_date = search_date;
    }

    private String search_date="";
}
