package com.nit.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SendEmailNotification {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmation(String to,
                                      String productName,
                                      Integer quantity,
                                      Double totalAmount,
                                      LocalDate orderDate,
                                      LocalDate shippingDate) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String htmlMsg = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>Order Confirmation</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f6f6f6; color: #333; }" +
                ".email-container { max-width: 600px; margin: 20px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }" +
                ".header { text-align: center; padding: 10px 0; background-color: #2874f0; color: #fff; border-radius: 8px 8px 0 0; }" +
                ".header h1 { margin: 0; font-size: 24px; }" +
                ".order-details { margin-top: 20px; }" +
                ".order-details h2 { font-size: 20px; color: #2874f0; }" +
                ".order-details p { font-size: 16px; line-height: 1.5; }" +
                ".order-summary { margin-top: 20px; border-top: 1px solid #ddd; padding-top: 10px; }" +
                ".order-summary table { width: 100%; border-collapse: collapse; }" +
                ".order-summary th, .order-summary td { padding: 8px 12px; text-align: left; }" +
                ".order-summary th { background-color: #f6f6f6; }" +
                ".footer { text-align: center; margin-top: 20px; font-size: 12px; color: #888; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +
                "<div class=\"header\">" +
                "<h1>Thank You for Your Purchase!</h1>" +
                "</div>" +
                "<div class=\"order-details\">" +
                "<h2>Order Confirmation</h2>" +
                "<p>Dear Customer,</p>" +
                "<p>Thank you for shopping with us! Your order has been successfully placed and is now being processed.</p>" +
                "</div>" +
                "<div class=\"order-summary\">" +
                "<h2>Order Summary</h2>" +
                "<table>" +
                "<tr><th>Product Name</th><td>{{productName}}</td></tr>" +
                "<tr><th>Quantity</th><td>{{quantity}}</td></tr>" +
                "<tr><th>Total Amount</th><td>{{totalAmount}}</td></tr>" +
                "<tr><th>Order Date</th><td>{{orderDate}}</td></tr>" +
                "<tr><th>Shipping Date</th><td>{{shippingDate}}</td></tr>" +
                "</table>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>If you have any questions or concerns, feel free to contact our customer support.</p>" +
                "<p>Thank you for choosing us!</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";


        htmlMsg = htmlMsg.replace("{{productName}}", productName)
                         .replace("{{quantity}}", quantity.toString())
                         .replace("{{totalAmount}}", totalAmount.toString())
                         .replace("{{orderDate}}", orderDate.toString())
                         .replace("{{shippingDate}}", shippingDate.toString());

        helper.setTo(to);
        helper.setSubject("Your Order Confirmation,"+productName+"!");
        // Set to true to send HTML content
        helper.setText(htmlMsg, true);

        mailSender.send(message);
    }

    public void sendEmailToAdmin(Integer quantity,String productName){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("syedfaizan7058@gmail.com");
        message.setSubject("Stock Alert : "+productName);
        message.setText("Dear Admin,\n\nThe stock for the product '" + productName + "' is running low.\nCurrent Stock: " + quantity
                + "\n\nPlease take necessary actions to restock the item.\n\nBest Regards,\nYour Inventory Management System");

        mailSender.send(message);
    }
}

