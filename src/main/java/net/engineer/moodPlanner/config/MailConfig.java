//package net.engineer.moodPlanner.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Optional;
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(Optional.ofNullable(System.getenv("SPRING_MAIL_HOST")).orElse("smtp.gmail.com"));
//        mailSender.setPort(Integer.parseInt(Optional.ofNullable(System.getenv("SPRING_MAIL_PORT")).orElse("587")));
//        mailSender.setUsername(Optional.ofNullable(System.getenv("SPRING_MAIL_USERNAME")).orElse("example@gmail.com"));
//        mailSender.setPassword(Optional.ofNullable(System.getenv("SPRING_MAIL_PASSWORD")).orElse("password"));
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "false");
//
//        return mailSender;
//    }
//}



//package net.engineer.moodPlanner.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//
//    @Bean
//    public JavaMailSender javaMailSender(@Value("${SPRING_MAIL_USERNAME}") String username,
//                                         @Value("${SPRING_MAIL_PASSWORD}") String password) {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.sendgrid.net");
//        mailSender.setPort(587);
//        mailSender.setUsername(username);
//        mailSender.setPassword(password);
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        return mailSender;
//    }
//
//}




package net.engineer.moodPlanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Optional;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Host, port, username, and password from environment variables
        mailSender.setHost(Optional.ofNullable(System.getenv("SPRING_MAIL_HOST")).orElse("smtp.gmail.com"));
        mailSender.setPort(Integer.parseInt(Optional.ofNullable(System.getenv("SPRING_MAIL_PORT")).orElse("587")));
        mailSender.setUsername(Optional.ofNullable(System.getenv("SPRING_MAIL_USERNAME")).orElse("example@gmail.com"));
        mailSender.setPassword(Optional.ofNullable(System.getenv("SPRING_MAIL_PASSWORD")).orElse("password"));

        // SMTP properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        System.out.println("SMTP Config -> Host: " + mailSender.getHost() + ", User: " + mailSender.getUsername());
        return mailSender;
    }
}
