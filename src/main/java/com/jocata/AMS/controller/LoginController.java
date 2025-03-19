package com.jocata.AMS.controller;

import com.jocata.AMS.forms.UserForm;
import com.jocata.AMS.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        UserForm userByEmail = userService.getUserByEmail(email);
        if(userByEmail != null ) {
            if(userByEmail.getPassword().equals(password)) {
                return "Welcome " + userByEmail.getEmail();
            }
            else {
                return "Wrong password";
            }
        }else {
            return "User not found , user have to register";
        }
    }

}
