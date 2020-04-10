package zju.group1.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class MailService {


    @Value("${spring.email.password}")
    private String password;
    @Value("${spring.email.username}")
    private String username;
    @Value("${spring.email.host}")
    private String host;

    public void sendToken(String toEmail, String token) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Forum注册验证");
        simpleMailMessage.setText("您的验证码是:" + token + "\n验证码30min内有效");
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.send(simpleMailMessage);

    }
}
