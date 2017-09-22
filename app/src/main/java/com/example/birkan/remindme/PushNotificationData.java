package com.example.birkan.remindme;

/**
 * Created by birkan on 22.09.2017.
 */

public class PushNotificationData {
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String Title;
    public String Message;
}
