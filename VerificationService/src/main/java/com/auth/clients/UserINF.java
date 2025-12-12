package com.auth.clients;

import com.auth.entity.UserCreationDTO;
import com.auth.entity.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-client",
        url = "https://user-service-di5e.onrender.com"
)
public interface UserINF {
    @PostMapping("/user")
    public ResponseEntity<UserCreationDTO> saveUser(@RequestBody UserDto user);

    @GetMapping("/user/username/{username}")
    public Long getIdByUsername(@PathVariable String username);

}