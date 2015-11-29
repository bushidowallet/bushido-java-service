package com.bitcoin.blockchain.api.mail;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Jesion on 2015-06-19.
 */
public class SendGridManager {

    @Value("${app.sendgrid.username}")
    private String username;

    @Value("${app.sendgrid.password}")
    private String password;

    public SendGridManager() {

    }

    public void send(String to, String from, String subject, String html) {
        SendGrid sendgrid = new SendGrid(username, password);
        SendGrid.Email email = new SendGrid.Email();
        email.addTo(to);
        email.setFrom(from);
        email.setSubject(subject);
        email.setHtml(html);
        try {
            SendGrid.Response response = sendgrid.send(email);
            System.out.println("SendGrid mail response: " + response.getMessage());
        } catch (SendGridException e) {
            System.out.println("Failed to send mail: " + e.toString());
        }
    }
}