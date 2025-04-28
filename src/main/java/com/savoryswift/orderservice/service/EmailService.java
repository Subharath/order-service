package com.savoryswift.orderservice.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendEmail(String toEmail, String subject, String content) throws IOException {
        Email from = new Email("your-verified-sendgrid-sender@example.com"); // 👈 use verified sender
        Email to = new Email(toEmail);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, to, emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("📧 Email sent! Status Code: " + response.getStatusCode());
        } catch (IOException ex) {
            System.err.println("❌ Error sending email: " + ex.getMessage());
            throw ex;
        }
    }
}
