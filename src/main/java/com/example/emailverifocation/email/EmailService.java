package com.example.emailverifocation.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailSender{

    private static Logger LOGGER= LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;




    @Override
    public void send(String to, String email) {

        try {
            MimeMessage mimeMessage=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("confirm your email//");


            javaMailSender.send(mimeMessage);


        }catch (Exception e){
            LOGGER.error("fail to send email",e);
            throw new IllegalStateException("fail to send emailllll");
        }
    }
}
