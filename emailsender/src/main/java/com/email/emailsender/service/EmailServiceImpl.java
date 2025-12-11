package com.email.emailsender.service;

import com.email.emailsender.entity.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailDto emailDto) {

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(emailDto.getTo());
            mail.setSubject(emailDto.getSubject());
            mail.setText(emailDto.getBody());
            javaMailSender.send(mail);

        }
        catch (Exception e){
            System.out.println("Exception while sending email" + e);
        }
    }
}
