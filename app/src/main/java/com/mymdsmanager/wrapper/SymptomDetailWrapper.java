package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 7/9/15.
 */
public class SymptomDetailWrapper {


    private int srowid = -1;

    public String getSymptomname() {
        return symptomname;
    }

    public void setSymptomname(String symptomname) {
        this.symptomname = symptomname;
    }

    public int getSrowid() {
        return srowid;
    }

    public void setSrowid(int srowid) {
        this.srowid = srowid;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSymptomdate() {
        return symptomdate;
    }

    public void setSymptomdate(String symptomdate) {
        this.symptomdate = symptomdate;
    }

    public String getSymptomtime() {
        return symptomtime;
    }

    public void setSymptomtime(String symptomtime) {
        this.symptomtime = symptomtime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public String getSubsymptom_str() {
        return subsymptom_str;
    }

    public void setSubsymptom_str(String subsymptom_str) {
        this.subsymptom_str = subsymptom_str;
    }

    private String symptomname = "", severity = "", symptomdate = "", symptomtime = "", duration = "", frequency = "", notes = "", createdate = "", modifieddate = "",subsymptom_str="";


}
