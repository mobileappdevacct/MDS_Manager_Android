package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 7/9/15.
 */
public class TreatmentInfoWrapper {

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getTreatement() {
        return treatement;
    }

    public void setTreatement(String treatement) {
        this.treatement = treatement;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private String startdate = "", enddate = "", treatement = "", notes = "";

    public int getTrowid() {
        return trowid;
    }

    public void setTrowid(int trowid) {
        this.trowid = trowid;
    }

    private int trowid=0;

}
