package com.jocata.AMS.controller;

import com.jocata.AMS.forms.UserForm;
import com.jocata.AMS.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public UserForm registerUser(@RequestBody UserForm userForm) {
        UserForm user = userService.createUser(userForm);
        if (user == null) {
            System.out.println("User creation failed");
            return null;
        }
        System.out.println("User registered");
        return user;
    }
    @GetMapping("/user/{id}")
    public UserForm getUser(@PathVariable("id") String id) {
        return userService.getUserById(Integer.valueOf(id));

    }
    @GetMapping("/admin/users")
    public List<UserForm> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/user")
    public UserForm getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping("/admin/upload")
    public String uploadProduct(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()) {
            return "file is empty";
        }
        try{
            userService.processExelFile(file);
            return "file successfully uploaded";
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return "File upload failed";
        }
    }
}
