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



package net.engineer.moodPlanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String host = System.getenv("SPRING_MAIL_HOST");          // e.g. smtp.gmail.com
        String port = System.getenv("SPRING_MAIL_PORT");          // e.g. 587
        String username = System.getenv("SPRING_MAIL_USERNAME");  // your email
        String password = System.getenv("SPRING_MAIL_PASSWORD");  // app password
        String protocol = System.getenv("SPRING_MAIL_PROTOCOL");  // optional

        mailSender.setHost(host != null ? host : "smtp.gmail.com");
        mailSender.setPort(port != null ? Integer.parseInt(port) : 587);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setProtocol(protocol != null ? protocol : "smtp");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailSender.getProtocol());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "false"); // Gmail TLS
        props.put("mail.debug", "false");

        return mailSender;
    }
}
