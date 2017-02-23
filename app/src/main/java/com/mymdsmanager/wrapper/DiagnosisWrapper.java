package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 9/9/15.
 */
public class DiagnosisWrapper {


    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getManagingprovider() {
        return managingprovider;
    }

    public void setManagingprovider(String managingprovider) {
        this.managingprovider = managingprovider;
    }

    public String getHistorytype() {
        return historytype;
    }

    public void setHistorytype(String historytype) {
        this.historytype = historytype;
    }

    public int getDrowid() {
        return drowid;
    }

    public void setDrowid(int drowid) {
        this.drowid = drowid;
    }

    private int drowid = -1;
    private String diagnosis = "", date = "", notes = "", managingprovider = "", historytype = "";
}
