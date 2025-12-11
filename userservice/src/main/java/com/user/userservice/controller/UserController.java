package com.user.userservice.controller;

import com.user.userservice.client.PaymentINF;
import com.user.userservice.client.WalletINF;
import com.user.userservice.entity.Transactions;
import com.user.userservice.entity.TransferRequestDTO;
import com.user.userservice.entity.User;
import com.user.userservice.entity.UserDto;
import com.user.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentINF paymentINF;

    @GetMapping("/getUserDetails")
    public ResponseEntity<User> getUser(HttpServletRequest request){
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            throw new RuntimeException("Missing X-User-Id header");
        }

        Long id;
        try {
            id = Long.valueOf(userIdHeader);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid X-User-Id header format");
        }
        User user = userService.getUserById(id);
        System.out.println(user.getDob());
        return ResponseEntity.ok(user);

    }

    @GetMapping("/getusername")
    public ResponseEntity<String> getusername(@RequestParam("id") Long id){



        String username = userService.getusername(id);

        return ResponseEntity.ok().body(username);

    }

    @GetMapping("/test")
    public String test(){
        return "Working";

    }
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody UserDto dto) {
        if (dto == null) {
            return null;
        }
        Random random = new Random();

        // Generate a number between 10000000 (inclusive) and 99999999 (inclusive)
        Long eightDigitNumber = (long) (10000000 + random.nextInt(90000000));

        System.out.println("Random 8-digit number: " + eightDigitNumber);


        User user = new User();
        user.setId(eightDigitNumber);
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setGender(dto.getGender());
        user.setKyc(dto.getKyc());
        user.setDob(dto.getDob());
        user.setPhoneNumber(dto.getPhoneNumber());

        User savedUser = userService.saveUser(user);
        userService.createWallet(savedUser.getId());

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /*
    tested   /user/1/balance
     */
    @GetMapping("/{id}/balance")
    public ResponseEntity<Long> getBalance(@PathVariable Long id){

        return ResponseEntity.ok(userService.getBalance(id));
    }
    @GetMapping("/username/{username}")
    public Long getIdByUsername(@PathVariable("username") String username){
        System.out.println("username: " + username + "   ************************   ");
        return userService.getIdByUsername(username);
    }

    @GetMapping("/payment/alltransactions/{id}")
    public ResponseEntity<List<Transactions>> getAllTransactionsOfUser(@PathVariable("id") Long id){
        System.out.println("Entered getAllTransactionsOfUser with id: " + id +" *************************************");
        return ResponseEntity.ok(paymentINF.getAllTransactionsOfUser(id));
    }

    @PostMapping("/credit")
    public ResponseEntity<String> credit(@RequestBody TransferRequestDTO transferRequestDTO){
        paymentINF.creditAmount(transferRequestDTO);
        return ResponseEntity.ok().body("Credited Successfully");
    }
    @PostMapping("/debit")
    public ResponseEntity<String> debit(@RequestBody TransferRequestDTO transferRequestDTO){
        paymentINF.debitAmount(transferRequestDTO);
        return ResponseEntity.ok().body("Debited Successfully");
    }

    @PostMapping("/transferamount")
    public ResponseEntity<String> transfer(@RequestBody TransferRequestDTO transferRequestDTO){
        System.out.println("UserController  transfer ************************1" + transferRequestDTO);
        paymentINF.transferAmount(transferRequestDTO);
        return ResponseEntity.ok().body("transfer amount Successfully");
    }
    @GetMapping("/getEmailById/{id}")
    String getEmail(@PathVariable Long id){
        return userService.getEmail(id);
    }

    @PutMapping("/edit")
    public Boolean edit(@RequestBody User user, HttpServletRequest request){
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            throw new RuntimeException("Missing X-User-Id header");
        }

        Long id;
        try {
            id = Long.valueOf(userIdHeader);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid X-User-Id header format");
        }
        user.setId(id);
        userService.edit(user);
        return true;
    }

}
