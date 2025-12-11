package com.bank.controller;

import com.bank.client.ApiClient;
import com.bank.entity.*;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RegisterController {

    @Autowired
    private ApiClient apiClient;

    @GetMapping("/")
    public String start() {

        return "index"; // maps to register.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        // Invalidate the session to remove JWT and all stored attributes
        session.invalidate();

        // Redirect to index page (default start page)
        return "redirect:/";
    }


    // Show registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register"; // maps to register.html
    }

    // Handle form submission
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userDto") UserDto userDto,
            BindingResult result,
            Model model) {

        // Step 1: Handle validation errors (Bean Validation)
        if (result.hasErrors()) {
            // redisplay the form with validation messages
            return "register";
        }

        String password = userDto.getPassword();
        String confirmPassord = userDto.getConfirmPassword();
        if(!password.equals(confirmPassord)){
            model.addAttribute("error", "ConfirmPassword should match with Password");
            return "register";
        }

        try {
            // Step 2: Call API client

            ResponseEntity<String> response = apiClient.userCreate(userDto);

            // Step 3: Add success message and reset form
            model.addAttribute("message", response.getBody());
            model.addAttribute("userDto", new UserDto());

            return "index"; // success page

        } catch (FeignException e) {

            String errorMessage = e.contentUTF8();
            int statusCode = e.status();

            model.addAttribute("error",  errorMessage);
            e.printStackTrace();

            // redisplay form with API error message
            return "register";

        } catch (Exception generalException) {
            // Step 5: Handle unexpected errors
            generalException.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");

            // redisplay form with generic error
            return "register";
        }
    }


    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());

        return "login";  // login.html
    }

    @PostMapping("/login")
    public String authenticateUser(@ModelAttribute LoginRequest loginRequest,
                                   Model model,
                                   HttpSession session) {
        try {

            LoginResponse response = apiClient.authenticateUser(loginRequest).getBody();


            if (response == null) {
                model.addAttribute("error", "Invalid username or password.");
                return "login";
            }


            session.setAttribute("JWT_TOKEN", response.getJwtToken());


            return "redirect:/dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Authentication failed!");
            return "login";
        }
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";  // dashboard.html
    }

    @GetMapping("/credit")
    public String creditForm(Model model) {
        model.addAttribute("request", new TransferRequestDTO());
        return "credit";
    }

    @PostMapping("/credit")
    public String creditAmount(@ModelAttribute TransferRequestDTO transferRequestDTO,
                               RedirectAttributes redirectAttributes) {
        try {


            Boolean success = apiClient.creditAmount(transferRequestDTO);

            if (Boolean.TRUE.equals(success)) {
                redirectAttributes.addFlashAttribute("message",
                        "Amount credited successfully!");
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error",
                    "Failed to credit amount.");
            e.printStackTrace();
        }finally {
            return "redirect:/dashboard";
        }


    }

    @GetMapping("/debit")
    public String debitPage(Model model) {
        model.addAttribute("transferRequestDTO", new TransferRequestDTO());
        return "debit";  // debit.html
    }

    @PostMapping("/debit")
    public String debitAmount(@ModelAttribute TransferRequestDTO transferRequestDTO,
                              RedirectAttributes redirectAttributes) {


        try {

            Boolean success = apiClient.debitAmount(transferRequestDTO);
            if (Boolean.TRUE.equals(success)) {
                redirectAttributes.addFlashAttribute("message",
                        "Amount debited successfully!");
            }
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error",
                    "Insufficient balance or debit failed!");
            e.printStackTrace();
        }finally {
            return "redirect:/dashboard";
        }



    }

    @GetMapping("/balance")
    public String checkBalance(RedirectAttributes redirectAttributes) {
        try {
            Long balance = apiClient.getBalance();

            redirectAttributes.addFlashAttribute("message",
                    "Your current balance is: ₹" + balance);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Unable to fetch balance. Please try again.");
        }

        return "redirect:/dashboard";
    }



    @GetMapping("/transfer")
    public String transferPage(Model model) {
        model.addAttribute("transferRequestDTO", new TransferRequestDTO());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String doTransfer(@ModelAttribute TransferRequestDTO transferRequestDTO,
                             RedirectAttributes redirectAttributes) {
        try{
            Boolean success = apiClient.transferAmount(transferRequestDTO);
            if (Boolean.TRUE.equals(success)) {
                redirectAttributes.addFlashAttribute("message",
                        "₹" + transferRequestDTO.getBalance() +
                                " transferred successfully to User ID " +
                                transferRequestDTO.getReceiverId() + "!");
            }
        } catch(Exception e){
            redirectAttributes.addFlashAttribute("error",
                    "Transfer failed! Check balance and receiver ID.");
            e.printStackTrace();
        }finally {
            return "redirect:/dashboard";
        }


    }

    @GetMapping("/transactions")
    public String viewTransactions(Model model) {
        try {
            List<Transactions> transactions = apiClient.getAllTransactions();
            model.addAttribute("transactions", transactions);
        } catch (Exception e) {
            model.addAttribute("error", "Unable to load transactions!");
        }

        return "transactions";
    }

    @GetMapping("/details")
    public String viewUserDetails(Model model) {
        try {
            // Assuming JWT is stored in session and ApiClient can fetch user details

            User user = apiClient.getUser().getBody(); // implement in ApiClient
            System.out.println("DOB: " + user.getDob());
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", "Unable to load user details!");
            e.printStackTrace();
        }

        return "details";
    }


    @GetMapping("/details/edit")
    public String showEditForm(Model model) {
        try {

            User user = apiClient.getUser().getBody(); // fetch current user details
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", "Unable to load user details for editing!");
        }
        return "edit-details"; // maps to edit-details.html
    }

    @PostMapping("/details/edit")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        if (result.hasErrors()) {
            // redisplay the form with validation messages
            return "edit-details";
        }

        try {

            Boolean success = apiClient.edit(user); // implement in ApiClient

            if (Boolean.TRUE.equals(success)) {
                System.out.println("DOB: " + user.getDob());
                redirectAttributes.addFlashAttribute("message", "Details updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to update details!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating details!");
            e.printStackTrace();
        }
        return "redirect:/details"; // back to details page
    }

}
