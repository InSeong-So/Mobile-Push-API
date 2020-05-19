package com.sis.api.push.model;

import java.util.Date;

public class DeviceVO
{
    private String UUID;
    
    private String TOKEN;
    
    private String OS;
    
    private Date AGRDT;
    
    private String RCVYN;
    
    private String APPID;
    
    private String MBRID;
    
    public DeviceVO()
    {
    }
    
    public DeviceVO(String uUID, String tOKEN, String oS, Date aGRDT, String rCVYN, String aPPID, String mBRID)
    {
        super();
        UUID = uUID;
        TOKEN = tOKEN;
        OS = oS;
        AGRDT = aGRDT;
        RCVYN = rCVYN;
        APPID = aPPID;
        MBRID = mBRID;
    }
    
    public String getUUID()
    {
        return UUID;
    }
    
    public void setUUID(String uUID)
    {
        UUID = uUID;
    }
    
    public String getTOKEN()
    {
        return TOKEN;
    }
    
    public void setTOKEN(String tOKEN)
    {
        TOKEN = tOKEN;
    }
    
    public String getOS()
    {
        return OS;
    }
    
    public void setOS(String oS)
    {
        OS = oS;
    }
    
    public Date getAGRDT()
    {
        return AGRDT;
    }
    
    public void setAGRDT(Date aGRDT)
    {
        AGRDT = aGRDT;
    }
    
    public String getRCVYN()
    {
        return RCVYN;
    }
    
    public void setRCVYN(String rCVYN)
    {
        RCVYN = rCVYN;
    }
    
    public String getAPPID()
    {
        return APPID;
    }
    
    public void setAPPID(String aPPID)
    {
        APPID = aPPID;
    }
    
    public String getMBRID()
    {
        return MBRID;
    }
    
    public void setMBRID(String mBRID)
    {
        MBRID = mBRID;
    }
    
}
