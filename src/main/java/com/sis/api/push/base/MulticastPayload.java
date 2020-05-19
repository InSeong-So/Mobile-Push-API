package com.sis.api.push.base;

import java.util.List;

public class MulticastPayload extends PayloadBase
{
    
    private List<String> registration_ids;
    
    public List<String> getRegistration_ids()
    {
        return registration_ids;
    }
    
    public void setRegistration_ids(List<String> registration_ids)
    {
        this.registration_ids = registration_ids;
    }
    
}
