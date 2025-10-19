//package net.engineer.moodPlanner.service;
//
//import com.sendgrid.*;
//import com.sendgrid.helpers.mail.Mail;
//import com.sendgrid.helpers.mail.objects.Content;
//import com.sendgrid.helpers.mail.objects.Email;
//import com.sendgrid.helpers.mail.objects.Personalization;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//public class SendGridEmailService {
//
//    private final String SENDGRID_API_KEY = System.getenv("SENDGRID_API_KEY");
//
//    public void sendVerificationEmail(String toEmail, String token) throws IOException {
//        Email from = new Email("mdsaifalikhan494@gmail.com");
//        String subject = "Email Verification";
//
//        Email to = new Email(toEmail);
//        Content content = new Content("text/plain", "Click the link to verify your email: " +
//                "http://localhost:8080/auth/verify?token=" + token);
//
//        Mail mail = new Mail();
//        mail.setFrom(from);
//        mail.setSubject(subject);
//        mail.addContent(content);
//
//        Personalization personalization = new Personalization();
//        personalization.addTo(to);
//        mail.addPersonalization(personalization);
//
//        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
//        Request request = new Request();
//
//        request.setMethod(Method.POST);
//        request.setEndpoint("mail/send");
//        request.setBody(mail.build());
//
//        sg.api(request);
//    }
//}
