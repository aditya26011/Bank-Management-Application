package com.auth.controller;

import com.auth.clients.UserINF;
import com.auth.entity.LoginRequest;
import com.auth.entity.LoginResponse;
import com.auth.entity.UserDto;
import com.auth.security.jwt.JwtUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bank")
public class Controller {

    @Autowired
    private UserINF userINF;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserDetailsManager userDetailsManager;


    @GetMapping("/hello")
    public String  sayHello(){
        return "I am working";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String  userHello(){
        return "Hello, User";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String  adminHello(){
        return "Hello, Admin";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));

        }catch ( AuthenticationException e){
            Map<String, Object> map = new HashMap<>();
            map.put("message","Bad credentials");
            map.put("status",false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        Long id = userINF.getIdByUsername(userDetails.getUsername());
        LoginResponse response = new LoginResponse(jwtToken, id, userDetails.getUsername(), roles);

        return  ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> userCreate(@RequestBody UserDto userDto){
        if(userDetailsManager.userExists(userDto.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists, try again with new Username!");
        }

        UserDetails userDetails = User.withUsername(userDto.getUsername()).password(encoder.encode(userDto.getPassword())).roles("ADMIN")
                .build();
        userDetailsManager.createUser(userDetails);

        userINF.saveUser(userDto);


        return ResponseEntity.status(HttpStatus.CREATED).body("User : " +userDto.getUsername() + "  Registered Successfully");
    }

    @GetMapping("/verifytoken")
    public Boolean verigyToken(@RequestParam("token") String token){
        return jwtUtils.validateJwtToken(token);
    }
}
