package com.transaction.client;

import com.transaction.entity.EmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url= "https://email-service-p356.onrender.com", value = "email-client")
public interface NotifyINF {

    @PostMapping("/notify")
    void sendEmail(@RequestBody EmailDTO emailDTO);
}