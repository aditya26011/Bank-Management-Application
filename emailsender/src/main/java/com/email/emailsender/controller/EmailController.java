package com.email.emailsender.controller;

import com.email.emailsender.entity.EmailDto;
import com.email.emailsender.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    ResponseEntity<String> sendEmail(@RequestBody EmailDto emailDto){
       emailService.sendEmail(emailDto);
        return ResponseEntity.ok().body("Sent email succesfully");
    }
}
