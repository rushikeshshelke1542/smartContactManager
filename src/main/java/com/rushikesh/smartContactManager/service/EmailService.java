package com.rushikesh.smartContactManager.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    

    public boolean sendOTPEmail(String recipientEmail, String otp) {
        boolean flag = false;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp);

        javaMailSender.send(message);
        
        flag = true;

   return flag; }
}

