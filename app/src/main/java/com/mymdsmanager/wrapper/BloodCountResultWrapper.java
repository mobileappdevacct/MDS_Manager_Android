package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 5/9/15.
 */
public class BloodCountResultWrapper {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHgb() {
        return hgb;
    }

    public void setHgb(String hgb) {
        this.hgb = hgb;
    }

    public String getWbc() {
        return wbc;
    }

    public void setWbc(String wbc) {
        this.wbc = wbc;
    }

    public String getAnc() {
        return anc;
    }

    public void setAnc(String anc) {
        this.anc = anc;
    }

    public String getPlatelets() {
        return platelets;
    }

    public void setPlatelets(String platelets) {
        this.platelets = platelets;
    }

    public String getRbcs() {
        return rbcs;
    }

    public void setRbcs(String rbcs) {
        this.rbcs = rbcs;
    }

    public String getTranfusion() {
        return tranfusion;
    }

    public void setTranfusion(String tranfusion) {
        this.tranfusion = tranfusion;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFerritin() {
        return ferritin;
    }

    public void setFerritin(String ferritin) {
        this.ferritin = ferritin;
    }


    public int getBroid() {
        return broid;
    }

    public void setBroid(int broid) {
        this.broid = broid;
    }

    private int broid = -1;
    private String date = "";
    private String hgb = "";
    private String wbc = "";
    private String anc = "";
    private String platelets = "";
    private String rbcs = "";
    private String tranfusion = "";
    private String notes = "";
    private String ferritin = "";
    private String blood_type="";
    private String blood_notes="";

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    private String image_path="";

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getBlood_notes() {
        return blood_notes;
    }

    public void setBlood_notes(String blood_notes) {
        this.blood_notes = blood_notes;
    }
}
