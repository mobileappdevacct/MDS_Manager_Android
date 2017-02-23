package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 9/9/15.
 */
public class LabResultInfoWrapper {


    private int lrowid = 0;

    public String getDiagnosistest() {
        return diagnosistest;
    }

    public void setDiagnosistest(String diagnosistest) {
        this.diagnosistest = diagnosistest;
    }

    public int getLrowid() {
        return lrowid;
    }

    public void setLrowid(int lrowid) {
        this.lrowid = lrowid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLabimages() {
        return labimages;
    }

    public void setLabimages(String labimages) {
        this.labimages = labimages;
    }

    public String getLabresulttype() {
        return labresulttype;
    }

    public void setLabresulttype(String labresulttype) {
        this.labresulttype = labresulttype;
    }

    private String diagnosistest = "", date = "", results = "", units = "", notes = "", labimages = "", labresulttype = "";
}
