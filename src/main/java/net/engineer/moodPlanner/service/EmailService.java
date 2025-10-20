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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${SPRING_MAIL_FROM}")
    private String fromEmail;

    public void sendVerificationEmail(String to, String link) {
        try {
            String sendGridUrl = "https://api.sendgrid.com/v3/mail/send";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(sendGridApiKey);

            Map<String, Object> body = Map.of(
                    "personalizations", new Object[]{
                            Map.of("to", new Object[]{Map.of("email", to)})
                    },
                    "from", Map.of("email", fromEmail),
                    "subject", "Verify your MoodPlanner account",
                    "content", new Object[]{
                            Map.of("type", "text/html",
                                    "value", "<p>Click <a href='" + link + "'>here</a> to verify your account.</p>")
                    }
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(sendGridUrl, HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("SendGrid API failed: " + response.getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send verification email via SendGrid API", e);
        }
    }
}
