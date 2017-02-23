package com.mymdsmanager.wrapper;

import java.io.Serializable;

/**
 * Created by suarebits on 2/11/15.
 */
public class TreatmentMedicineInfoWrapper implements Serializable {
    String medicinename = "";
    String dosage = "";
    String days = "";
    String cyclenumber = "";
    String notes = "";
    String date = "";
    String type = "";
    String othertreatmentname="";
    int treatementid = 0;

    public int getRemovePos() {
        return removePos;
    }

    public void setRemovePos(int removePos) {
        this.removePos = removePos;
    }

    int removePos;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id="";
    public String getOthertreatmentname() {
        return othertreatmentname;
    }

    public void setOthertreatmentname(String othertreatmentname) {
        this.othertreatmentname = othertreatmentname;
    }





    public int getTreatementid() {
        return treatementid;
    }

    public void setTreatementid(int treatementid) {
        this.treatementid = treatementid;
    }

    public String getMedicinename() {
        return medicinename;
    }

    public void setMedicinename(String medicinename) {
        this.medicinename = medicinename;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCyclenumber() {
        return cyclenumber;
    }

    public void setCyclenumber(String cyclenumber) {
        this.cyclenumber = cyclenumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
