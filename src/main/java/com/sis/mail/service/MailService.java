package com.sis.mail.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.websocket.Session;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.springframework.boot.rsocket.server.RSocketServer.Transport;

import com.sis.mail.model.MailVO;

public class MailService
{
    /**
     * 로그를 남기기 위한 로그 객체
     */
    static Logger log = Logger.getRootLogger();
    
    /**
     * 메일 정보을 담고 있는 맵
     */
    MailVO MailVO = null;
    
    /**
     * 생성자
     * 
     * @param hashMap
     */
    public MailUtil(MailVO MailVO)
    {
        this.MailVO = MailVO;
    }
    
    /**
     * 직접 메일을 발송한다.
     * 파라미터 HashMap 는 hashMap smtp_host, from_name, from, recipients, subject,
     * content를 포함해야 한다.
     * HashMap에 mail_auth_id, mail_auth_pwd가 포함되지 않았을 경우는 메일인증을 받지 못해 메일이 나가지 못할
     * 수도 있으므로 가능하면 mail_auth_id로 메일인증을 받는것이 좋다.
     * 
     * @param hashMap
     *            메일정보
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @see MailAuthenticator
     */
    public static void sendMailA(MailVO MailVO)
    {
        //설정
        String email_type = MailVO.getEmail_type(); //host
        
        String smtp_host = MailVO.getSmtp_host(); // host
        String smtp_port = MailVO.getSmtp_port(); // port
        String from_name = MailVO.getFrom_name(); // 보내는사람
        String from = MailVO.getFrom(); // 보내는 메일주소
        
        String recipients = MailVO.getRecipients(); // 받는 사람
        String[] arrRecipient = recipients.split(",");
        
        String recipientsCC = MailVO.getRecipientsCC();; //참조자
        String[] arrRecipientCC = recipientsCC.split(",");
        
        String recipientsBCC = MailVO.getRecipientsBCC();//숨은참조
        String[] arrRecipientBCC = recipientsBCC.split(",");
        
        
        String subject = MailVO.getSubject(); // 제목
        String content = MailVO.getContent(); // 내용
        
        String mail_auth_id = MailVO.getMail_auth_id(); // 메일 아이디
        String mail_auth_pw = MailVO.getMail_auth_pw(); // 메일 비밀번호
        MailAuthenticator MailAuthenticator = new MailAuthenticator(mail_auth_id, mail_auth_pw);
        
        log.debug("smtp_host[" + smtp_host + "] from_name[" + from_name + "] from[" 
                        + from + "] recipients[" + recipients + "] subject[" + subject + "] recipientsCC["
                        + recipientsCC + "] recipientsBCC["+ recipientsBCC + "]"
        );
        
        Session session = null;
        MimeMessage message = null;
        File file = null;
        String exception_msg = "";
        String file_nm = null;
        
        Properties props = new Properties();
        try {
            //메일 포트 설정
            props.put("mail.smtp.host", smtp_host);
            
            if ("465".equals(smtp_port))
            {
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
            else if ("587".equals(smtp_port))
            {
                props.put("mail.smtp.starttls.enable", "true");
            }
            props.put("mail.smtp.port", smtp_port);
            
            //세션설정            
            if (mail_auth_id != null && mail_auth_pw != null)
            {
                props.put("mail.smtp.auth", "true");
                session = Session.getInstance(props, MailAuthenticator);
            }
            else
            {
                session = Session.getInstance(props);
            }
            
            message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(from, from_name, "utf-8"));
            
            message.setSubject(subject, "utf-8"); //제목
            
            message.setSentDate(new Date());
            
            
            //받는사람
            InternetAddress[] arrInternetAddress = new InternetAddress[arrRecipient.length];
            for (int n = 0, nlen = arrRecipient.length; n < nlen; n++)
            {
                arrInternetAddress[n] = new InternetAddress(arrRecipient[n]);
            }
            message.addRecipients(Message.RecipientType.TO, arrInternetAddress);
            
            
            if (arrRecipientCC.length > 0)
            {
                //참조자
                InternetAddress[] arrInternetAddressCC = new InternetAddress[arrRecipientCC.length];
                
                for (int n = 0, nlen = arrRecipientCC.length; n < nlen; n++)
                {
                    arrInternetAddressCC[n] = new InternetAddress(arrRecipientCC[n]);
                }
                message.addRecipients(Message.RecipientType.CC, arrInternetAddressCC);
            }
            
            if (arrRecipientBCC.length > 0)
            {
                //숨은참조
                InternetAddress[] arrInternetAddressBCC = new InternetAddress[arrRecipientBCC.length];
                for (int n = 0, nlen = arrRecipientBCC.length; n < nlen; n++)
                {
                    arrInternetAddressBCC[n] = new InternetAddress(arrRecipientBCC[n]);
                }
                message.addRecipients(Message.RecipientType.BCC, arrInternetAddressBCC);
            }
            
            
            // 파일이 존재하면 메세지를 쪼개어 텍스트부와 파일부를 합쳐 보냄
            if (MailVO.getFile().isFile())
            {
                file = MailVO.getFile();
                file_nm = file.getName();
                
                MimeBodyPart mbp1 = null;
                FileDataSource fds = null;
                
                MimeBodyPart mbp2 = null;
                Multipart mp = null;
                
                mbp1 = new MimeBodyPart();
                mbp1.setText(content);
                
                fds = new FileDataSource(file.getAbsolutePath());
                
                mbp2 = new MimeBodyPart();
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(fds.getName());
                //혹시라도 첨부파일이 깨지게 되면
                //mbp2.setFileName(MimeUtility.encodeText(fds.getName(),"EUC-KR","B"));
                
                mp = new MimeMultipart();
                
                mp.addBodyPart(mbp1);
                mp.addBodyPart(mbp2);
                
                
                message.setContent(mp);
            }
            else
            {
                message.setContent(content, "text/html; charset=utf-8");
            }
            
            
        }catch(MessagingException e) {
            log.debug("The mail has errored. : " + e.getMessage());
            exception_msg = "[EMAIL-01]";
        }catch(UnsupportedEncodingException e) {
            log.debug("The mail has errored. : " + e.getMessage());
            exception_msg = "[EMAIL-02]";
        }catch(Exception e) {
            log.debug("The mail has errored. : " + e.getMessage());
            exception_msg = "[EMAIL-03]";
        }
        
        log.debug("==================");
        log.debug("mail setting done");
        log.debug("==================");
        
        try
        {
                if(!exception_msg.equals("")) {
                        Transport.send(message);
                }
        }
        catch (MessagingException e)
        {
            try
            {
                Transport.send(message);
            }
            catch (MessagingException e1)
            {
                try
                {
                    Transport.send(message);
                }
                catch (MessagingException e2)
                {
                    e.printStackTrace();
                    exception_msg = "[EMAIL-04]";
                }
            }
        }
        finally
        {
            
        }
        
    }
    
    /**
     * 메일을 직접 발송하지 않고 쓰레드로 보낸다.
     * 쓰레드로 보내면 메일 발송 에러가 발생해도 무시된다.
     * 
     * @param hashMap
     */
    public static void runSendMailThread(MailVO MailVO)
    {
        log.debug("java thread");
        new Thread(new MailUtil(MailVO)).start();
    }
    
    /**
     * sendMailA를 실행한다.
     * 
     * @see sendMailA
     */
    public void run()
    {
        try
        {
            sendMailA(MailVO);
        }
        catch (Exception e)
        {
            log.error(this.getClass().getName() + ".run()", e);
        }
    }
    
    /**
     * id/pw가 있는 SMTP서버에 로그인
     * 
     */
    static class MailAuthenticator extends javax.mail.Authenticator
    {
        String id, pw;
        
        MailAuthenticator(String id, String pw)
        {
            this.id = id;
            this.pw = pw;
        }
        
        protected javax.mail.PasswordAuthentication getPasswordAuthentication()
        {
            return new javax.mail.PasswordAuthentication(id, pw);
        }
    }
}