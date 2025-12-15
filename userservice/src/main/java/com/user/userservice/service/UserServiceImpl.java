package com.user.userservice.service;

import com.user.userservice.client.WalletINF;
import com.user.userservice.entity.User;
import com.user.userservice.entity.UserDto;
import com.user.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletINF walletINF;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();

    }

    @Override
    public User saveUser(User user) {
        return  userRepository.save(user);
    }

    @Override
    public Long getBalance(Long id) {
        return walletINF.getBalance(id);
    }

    @Override
    public Long getIdByUsername(String username) {
        System.out.println("Here flag 1 ****************");
        User user = userRepository.findByUsername(username);
        if(user != null){
            System.out.println("Fetched ************ " + user);
            return user.getId();
        }
        return null;
    }

    @Override
    public String getEmail(Long id) {
        return userRepository.getEmailById(id);
    }

    @Override
    public void createWallet(Long id) {
        walletINF.createWallet(id);
    }

    @Override
    @Transactional
    public void edit(User user) {

        User userF = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userF.setUsername(user.getUsername());
        userF.setName(user.getName());
        userF.setEmail(user.getEmail());
        userF.setGender(user.getGender());
        userF.setKyc(user.getKyc());
        userF.setDob(user.getDob());

        userRepository.save(userF);
    }

    @Override
    public String getusername(Long id) {
        return userRepository.getUsernameById(id);
    }


}
