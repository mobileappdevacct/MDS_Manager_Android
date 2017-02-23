package com.mymdsmanager.wrapper;

/**
 * Created by nitin on 7/9/15.
 */
public class MessagesWrapper {

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    private String notification = "", createdon = "";

}
