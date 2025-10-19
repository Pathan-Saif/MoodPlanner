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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String verifyLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(System.getenv("SPRING_MAIL_FROM")); // must be same as SMTP username
        helper.setTo(toEmail);
        helper.setSubject("Verify your MoodPlanner account");
        String html = "<p>Welcome — click to verify:</p>"
                + "<p><a href=\"" + verifyLink + "\">Verify your email</a></p>"
                + "<p>This link expires in " + (System.getenv("VERIFICATION_TOKEN_EXP_MS") != null ? System.getenv("VERIFICATION_TOKEN_EXP_MS") : "3600000") + " ms.</p>";
        helper.setText(html, true);
        mailSender.send(message);
    }

}
