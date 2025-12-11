package com.transaction.client;

import com.transaction.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url= "http://localhost:8081", value = "user-client")
@Component
public interface UserINF {


    @GetMapping("/user/getEmailById/{id}")
    String getEmail(@PathVariable Long id);

    @GetMapping("/user/getusername")
    public ResponseEntity<String> getusername(@RequestParam("id") Long id);

}