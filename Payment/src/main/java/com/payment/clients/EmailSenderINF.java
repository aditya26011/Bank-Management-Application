package com.payment.clients;

import com.payment.entities.EmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url= "https://email-service-p356.onrender.com", value = "email-client")
public interface EmailSenderINF {

    @PostMapping("/notify")
    ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO);
}
