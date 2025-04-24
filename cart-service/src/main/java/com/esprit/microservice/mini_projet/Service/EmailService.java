package com.esprit.microservice.mini_projet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendOrderConfirmationEmail(String to, String orderId, String total) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Order Confirmation - " + orderId);

        // Path to the logo (optional)

        // Add HTML content with an image tag using cid:logo if needed
        String htmlMsg = "<html><body>"
                + "<h1>Your Order</h1>"
                + "<p>Your order <strong>" + orderId + "</strong> has been successfully received.</p>"
                + "<p>Total: " + total + "</p>"
                + "</body></html>";

        helper.setText(htmlMsg, true); // true = HTML

        emailSender.send(message);
    }
}
