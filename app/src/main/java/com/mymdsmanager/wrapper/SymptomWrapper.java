package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 7/9/15.
 */
public class SymptomWrapper {
    private int srowid = -1;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSrowid() {
        return srowid;
    }

    public void setSrowid(int srowid) {
        this.srowid = srowid;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }
    private String symptom = "";

    private String description = "";

}
