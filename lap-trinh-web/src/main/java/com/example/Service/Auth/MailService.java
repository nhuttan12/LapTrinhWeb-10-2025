package com.example.Service.Auth;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailService {
    private final String email = "phamnhuttan.9a6.2017@gmail.com";
    private final String appPassword = "pvqm yoom rfog kgcj";
    private final String smtpHost;
    private final String smtpPort;

    public MailService() {
        this.smtpHost = "smtp.gmail.com"; // or your SMTP host
        this.smtpPort = "587";             // TLS port
    }

    public void sendEmail(String to, String subject, String messageText) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, appPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(messageText);

        Transport.send(message);
    }

}
