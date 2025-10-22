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


//package net.engineer.moodPlanner.service;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//@Service
//public class EmailService {
//
//
//
//
//    @Value("${SENDGRID_API_KEY}")
//    private String sendGridApiKey;
//
//    @Value("${SPRING_MAIL_FROM}")
//    private String fromEmail;
//
//    /**
//     * Send verification email via SendGrid REST API
//     * @param to    recipient email
//     * @param verifyLink  verification link
//     */
//    public void sendVerificationEmail(String to, String verifyLink) {
//        try {
//            if (fromEmail == null || fromEmail.isEmpty()) {
//                throw new RuntimeException("From email is not configured. Set SPRING_MAIL_FROM in your env.");
//            }
//
//            String sendGridUrl = "https://api.sendgrid.com/v3/mail/send";
//
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setBearerAuth(sendGridApiKey);
//
//            // Build SendGrid payload
//            Map<String, Object> body = Map.of(
//                    "personalizations", new Object[]{
//                            Map.of("to", new Object[]{Map.of("email", to)})
//                    },
//                    "from", Map.of("email", fromEmail),
//                    "subject", "Verify your MoodPlanner account",
//                    "content", new Object[]{
//                            Map.of("type", "text/html",
//                                    "value", "<p>Hello,</p>" +
//                                            "<p>Click <a href='" + verifyLink + "'>here</a> to verify your account.</p>" +
//                                            "<p>If you did not register, ignore this email.</p>")
//                    }
//            );
//
//            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
//            ResponseEntity<String> response = restTemplate.exchange(sendGridUrl, HttpMethod.POST, entity, String.class);
//
//            if (!response.getStatusCode().is2xxSuccessful()) {
//                throw new RuntimeException("SendGrid API failed: " + response.getStatusCode() +
//                        " - " + response.getBody());
//            }
//
//            System.out.println("Verification email sent to: " + to);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to send verification email via SendGrid API", e);
//        }
//    }
//
////    @PostConstruct
////    public void init() {
////        System.out.println("Loaded fromEmail: " + fromEmail);
////    }
//
//}




package net.engineer.moodPlanner.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${SPRING_MAIL_FROM}")
    private String fromEmail;

    @Value("${FRONTEND_BASE_URL}")
    private String frontendBaseUrl;

    public void sendVerificationEmail(String toEmail, String token) throws MessagingException {
        String subject = "Verify your MoodPlanner account";
        String verifyUrl = frontendBaseUrl + "/verify?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        System.out.println("Sending from email: " + fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(
                "<p>Hi,</p>" +
                        "<p>Please click the link below to verify your email:</p>" +
                        "<a href=\"" + verifyUrl + "\">Verify Now</a>" +
                        "<p>If you did not request this, ignore this email.</p>",
                true
        );

        mailSender.send(message);
    }
}
