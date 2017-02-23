package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 5/9/15.
 */
public class BoneMarrowResultWrapper {

    public String getMarrowblast() {
        return marrowblast;
    }

    public void setMarrowblast(String marrowblast) {
        this.marrowblast = marrowblast;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBoneimages() {
        return boneimages;
    }

    public void setBoneimages(String boneimages) {
        this.boneimages = boneimages;
    }

    private String date = "";

    public int getBrowid() {
        return browid;
    }

    public void setBrowid(int browid) {
        this.browid = browid;
    }

    private int browid = -1;
    private String marrowblast = "";
    private String description = "";
    private String notes = "";
    private String boneimages = "";
}
