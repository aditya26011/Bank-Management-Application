package com.transaction.client;

import com.transaction.entity.EmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url= "http://localhost:8085", value = "email-client")
public interface NotifyINF {

    @PostMapping("/notify")
    void sendEmail(@RequestBody EmailDTO emailDTO);
}