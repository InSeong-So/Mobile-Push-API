package com.sis.api.push.base;

public class PayloadBase
{
    
    private Notification notification;
    
    private Data data;
    
    public Notification getNotification()
    {
        return notification;
    }
    
    public void setNotification(Notification notification)
    {
        this.notification = notification;
    }
    
    public Data getData()
    {
        return data;
    }
    
    public void setData(Data data)
    {
        this.data = data;
    }
    
    public static class Notification
    {
        public String title;
        
        public String body;
        
        public String sound;
    }
    
    public static class Data
    {
        public String message;
        
        public String link;
        
        public String image;
    }
    
}
