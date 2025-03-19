package com.jocata.AMS.service;

import com.jocata.AMS.entity.User;
import com.jocata.AMS.forms.UserForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public UserForm createUser(UserForm user);
    public UserForm getUserByEmail(String email);
    public UserForm getUserById(Integer id);
    public List<UserForm> getAllUsers();
    public void processExelFile(MultipartFile file);
}
