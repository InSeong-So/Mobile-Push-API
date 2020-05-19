package com.sis.api.push.model;

import java.util.Date;

public class AppInfoVO
{
    
    private String APPID;
    
    private String APIKEY;
    
    private Date CRDT;
    
    private String TOKEN;
    
    public String getAPPID()
    {
        return APPID;
    }
    
    public void setAPPID(String aPPID)
    {
        APPID = aPPID;
    }
    
    public String getAPIKEY()
    {
        return APIKEY;
    }
    
    public void setAPIKEY(String aPIKEY)
    {
        APIKEY = aPIKEY;
    }
    
    public Date getCRDT()
    {
        return CRDT;
    }
    
    public void setCRDT(Date cRDT)
    {
        CRDT = cRDT;
    }
    
    public String getTOKEN()
    {
        return TOKEN;
    }
    
    public void setTOKEN(String tOKEN)
    {
        TOKEN = tOKEN;
    }
    
}
