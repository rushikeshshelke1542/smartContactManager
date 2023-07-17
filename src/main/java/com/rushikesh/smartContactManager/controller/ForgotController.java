package com.rushikesh.smartContactManager.controller;

import java.security.SecureRandom;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rushikesh.smartContactManager.dao.UserRepository;
import com.rushikesh.smartContactManager.entities.User;
import com.rushikesh.smartContactManager.helper.message;
import com.rushikesh.smartContactManager.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {

   
    private final EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

   
    public ForgotController(EmailService emailService) {
        this.emailService = emailService;
    }
    
    @GetMapping("/forgot")
    public String forgotPassword(){

        return "forgot_form";
    }   


    @PostMapping("/forgot-password")
    public String sendOTP(@RequestParam("email") String email, HttpSession session){

         // Create a SecureRandom object
        SecureRandom secureRandom = new SecureRandom();


        // Generate a random integer (for example, a 6-digit OTP)
       int otp = secureRandom.nextInt(999999);

        User user = this.userRepository.getUserByEmail(email);

        
        if(user==null){

            session.setAttribute("message", new message("User is not registered with email "+email, "alert-danger"));

        return "forgot_form";

      }
     else{

        emailService.sendOTPEmail(email, String.valueOf(otp));

        session.setAttribute("message", new message("Please Enter OTP shared to your email", "alert-success"));

       session.setAttribute("sessionOtp", otp);
       session.setAttribute("email", email);


       return "verify_otp"; }


       
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") Integer otp, HttpSession session){

        Integer sessionOtp = (int) session.getAttribute("sessionOtp");

     
        if(Objects.equals(otp, sessionOtp)){

        session.setAttribute("message", new message("OTP Verified Successfully", "alert-success"));

    
       return "change_password"; }

       else{

        session.setAttribute("message", new message("Please Enter valid OTP", "alert-danger"));


        return "verify_otp";  }
}

@PostMapping("/change-password")
public String changePassword(@RequestParam("newPassword") String newPassword, HttpSession session){

    String email = (String) session.getAttribute("email");

    User user = this.userRepository.getUserByEmail(email);

    user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
    this.userRepository.save(user);

    session.setAttribute("message", new message("Password Changed Successfully", "alert-success"));

return "redirect:/login";
}

}
