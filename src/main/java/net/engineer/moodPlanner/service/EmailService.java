//package net.engineer.moodPlanner.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendVerificationEmail(String toEmail, String token) {
//        String subject = "Email Verification";
//        String link = "http://localhost:8080/auth/verify?token=" + token;
//
//        String body = "Click the link to verify your email: " + link;
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject(subject);
//        message.setText(body);
//
//        mailSender.send(message);
//    }
//
//}
//
//
//
//


package net.engineer.moodPlanner.service;

import io.jsonwebtoken.lang.Assert;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Assert.notNull(fromEmail, "From email must not be null"); // debug check

            helper.setFrom(fromEmail); // ← yaha exception aa raha tha
            helper.setTo(to);
            helper.setSubject("Verify your MoodPlanner account");
            helper.setText("Click this link to verify: " + link, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace(); // backend console me exact error
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
