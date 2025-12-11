package com.email.emailsender.service;

import com.email.emailsender.entity.EmailDto;

public interface EmailService {
    void sendEmail(EmailDto emailDto);
}
