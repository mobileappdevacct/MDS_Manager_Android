package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 9/9/15.
 */
public class NotesWrapper {


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getNrowid() {
        return nrowid;
    }

    public void setNrowid(int nrowid) {
        this.nrowid = nrowid;
    }

    private int nrowid = -1;
    private String date = "", topic = "", notes = "";
}
