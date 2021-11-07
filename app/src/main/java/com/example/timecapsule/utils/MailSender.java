package com.example.timecapsule.utils;


import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

//Use javaemail API to send real emails
public class MailSender extends Authenticator {

    private final String userName;
    private final String password;
    private Session session;

    public MailSender(String userName, String password) throws GeneralSecurityException {
        this.userName = userName;
        this.password = password;
        initialize();
    }

    private void initialize(){
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");// Connection agreement
        props.put("mail.smtp.host", "smtp.qq.com");// host name
        props.put("mail.smtp.port", 587);// port name
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "flase");
        props.put("mail.debug", "true");
        session = Session.getDefaultInstance(props, this);
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }


    public synchronized void sendMail(String subject, String body, String sender, String recipient) throws AddressException, MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setSubject(subject);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        message.setDataHandler(handler);

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.replace(" ","")));
        Transport.send(message); 

    }
}
