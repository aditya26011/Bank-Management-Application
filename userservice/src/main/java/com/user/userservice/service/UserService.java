package com.user.userservice.service;

import com.user.userservice.entity.User;
import com.user.userservice.entity.UserDto;

public interface UserService {
    User getUserById(Long id);

    User saveUser(User user);

    Long getBalance(Long id);

    Long getIdByUsername(String username);
    String getEmail(Long id);

    void createWallet(Long id);


    void edit(User user);

    String getusername(Long id);
}
