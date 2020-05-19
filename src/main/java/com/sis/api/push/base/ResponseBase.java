package com.sis.api.push.base;

public class ResponseBase
{
    
    private String code;
    
    private String message;
    
    public static final String RESULT_OK = "0000";
    
    public static final String RESULT_FAIL = "9999";
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
}
