package com.mymdsmanager.wrapper;

import java.io.Serializable;

/**
 * Created by nitin on 7/9/15.
 */
public class MedicineInfoWrapper implements Serializable{


    public String getScheduledays() {
        return scheduledays;
    }

    public void setScheduledays(String scheduledays) {
        this.scheduledays = scheduledays;
    }

    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    public String getGenericname() {
        return genericname;
    }

    public void setGenericname(String genericname) {
        this.genericname = genericname;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

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

    public String getRefildate() {
        return refildate;
    }

    public void setRefildate(String refildate) {
        this.refildate = refildate;
    }

    public String getPrescirbedby() {
        return prescirbedby;
    }

    public void setPrescirbedby(String prescirbedby) {
        this.prescirbedby = prescirbedby;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getRefilfrequency() {
        return refilfrequency;
    }

    public void setRefilfrequency(String refilfrequency) {
        this.refilfrequency = refilfrequency;
    }

    public String getStarttimestamp() {
        return starttimestamp;
    }

    public void setStarttimestamp(String starttimestamp) {
        this.starttimestamp = starttimestamp;
    }

    public String getEndtimestamp() {
        return endtimestamp;
    }

    public void setEndtimestamp(String endtimestamp) {
        this.endtimestamp = endtimestamp;
    }

    public String getReminderstring() {
        return reminderstring;
    }

    public void setReminderstring(String reminderstring) {
        this.reminderstring = reminderstring;
    }

    public String getRfrequencytitle() {
        return rfrequencytitle;
    }

    public void setRfrequencytitle(String rfrequencytitle) {
        this.rfrequencytitle = rfrequencytitle;
    }

    public String getRemindercounts() {
        return remindercounts;
    }

    public void setRemindercounts(String remindercounts) {
        this.remindercounts = remindercounts;
    }

    public String getReminderstarttime() {
        return reminderstarttime;
    }

    public void setReminderstarttime(String reminderstarttime) {
        this.reminderstarttime = reminderstarttime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String drugname = "";
    private String genericname = "";
    private String dosage = "";
    private String type = "";
    private String frequency = "";
    private String startdate = "";
    private String enddate = "";
    private String refildate = "";
    private String prescirbedby = "";
    private String notes = "";
    private String imagename = "";
    private String refilfrequency = "";
    private String starttimestamp = "";
    private String endtimestamp = "";
    private String reminderstring = "";
    private String rfrequencytitle = "";
    private String remindercounts = "";
    private String reminderstarttime = "";
    private String scheduledays = "";
    private String id="";
    private String remindersoundfile="";
    private String remindersoundindex="";

    public String getREMINDERIDS() {
        return REMINDERIDS;
    }

    public void setREMINDERIDS(String REMINDERIDS) {
        this.REMINDERIDS = REMINDERIDS;
    }

    private String REMINDERIDS;

    public String getRemindersoundfile() {
        return remindersoundfile;
    }

    public void setRemindersoundfile(String remindersoundfile) {
        this.remindersoundfile = remindersoundfile;
    }

    public String getRemindersoundindex() {
        return remindersoundindex;
    }

    public void setRemindersoundindex(String remindersoundindex) {
        this.remindersoundindex = remindersoundindex;
    }
}
