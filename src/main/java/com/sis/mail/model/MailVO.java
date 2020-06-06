package com.sis.mail.model;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;

public class MailVO
{
    private String email_type = "0000";
    
    @Value("${sis.mail.smtp_host}")
    private String smtp_host;
    
    @Value("${sis.mail.smtp_port}")
    private String smtp_port;
    
    @Value("${sis.mail.from_name}")
    private String from_name;
    
    @Value("${sis.mail.from}")
    private String from;
    
    private String recipients = "";
    
    private String subject = "";
    
    private String content = "";
    
    private String mail_auth_id = "";
    
    private String mail_auth_pw = "";
    
    private String recipientsCC = "";
    
    private String recipientsBCC = "";
    
    private File file;
    
    public String getEmail_type()
    {
        return email_type;
    }
    
    public void setEmail_type(String email_type)
    {
        this.email_type = email_type;
    }
    
    public String getSmtp_host()
    {
        return smtp_host;
    }
    
    public void setSmtp_host(String smtp_host)
    {
        this.smtp_host = smtp_host;
    }
    
    public String getSmtp_port()
    {
        return smtp_port;
    }
    
    public void setSmtp_port(String smtp_port)
    {
        this.smtp_port = smtp_port;
    }
    
    public String getFrom_name()
    {
        return from_name;
    }
    
    public void setFrom_name(String from_name)
    {
        this.from_name = from_name;
    }
    
    public String getFrom()
    {
        return from;
    }
    
    public void setFrom(String from)
    {
        this.from = from;
    }
    
    public String getRecipients()
    {
        return recipients;
    }
    
    public void setRecipients(String recipients)
    {
        this.recipients = recipients;
    }
    
    public String getSubject()
    {
        return subject;
    }
    
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getMail_auth_id()
    {
        return mail_auth_id;
    }
    
    public void setMail_auth_id(String mail_auth_id)
    {
        this.mail_auth_id = mail_auth_id;
    }
    
    public String getMail_auth_pw()
    {
        return mail_auth_pw;
    }
    
    public void setMail_auth_pw(String mail_auth_pw)
    {
        this.mail_auth_pw = mail_auth_pw;
    }
    
    public String getRecipientsCC()
    {
        return recipientsCC;
    }
    
    public void setRecipientsCC(String recipientsCC)
    {
        this.recipientsCC = recipientsCC;
    }
    
    public String getRecipientsBCC()
    {
        return recipientsBCC;
    }
    
    public void setRecipientsBCC(String recipientsBCC)
    {
        this.recipientsBCC = recipientsBCC;
    }
    
    public File getFile()
    {
        return file;
    }
    
    public void setFile(File file)
    {
        this.file = file;
    }
}
