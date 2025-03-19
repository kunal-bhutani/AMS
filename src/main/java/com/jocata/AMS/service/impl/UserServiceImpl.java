package com.jocata.AMS.service.impl;

import com.jocata.AMS.dao.UserDao;
import com.jocata.AMS.entity.User;
import com.jocata.AMS.enums.Role;
import com.jocata.AMS.forms.UserForm;
import com.jocata.AMS.service.UserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserForm createUser(UserForm user) {
        User user1 = userDao.createUser(userFormToEntity(user));
        return entityToUserForm(user1);
    }

    @Override
    public UserForm getUserByEmail(String email) {
        User userByEmail = userDao.getUserByEmail(email);
        return entityToUserForm(userByEmail);
    }

    @Override
    public UserForm getUserById(Integer id) {
        User userById = userDao.getUserById(id);
        return entityToUserForm(userById);
    }

    @Override
    public List<UserForm> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        List<UserForm> userForms = new ArrayList<>();
        for (User user : users) {
            userForms.add(entityToUserForm(user));
        }
        return userForms;
    }

    @Override
    public void processExelFile(MultipartFile file) {
        try(InputStream inputStream = file.getInputStream();
            Workbook workbook =  new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for(Row row : sheet) {
                if(row.getRowNum() == 0) continue;
                    User user = new User();
                    user.setUsername(row.getCell(0).getStringCellValue());
                    user.setEmail(row.getCell(1).getStringCellValue());
                    user.setPhoneNumber(row.getCell(2).getStringCellValue());
                    user.setAddress(row.getCell(3).getStringCellValue());
                    user.setNationality(row.getCell(4).getStringCellValue());
                    user.setPasswordHash(row.getCell(5).getStringCellValue());
                    user.setRole(Role.valueOf(row.getCell(6).getStringCellValue().toUpperCase()));
                    userDao.createUser(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User userFormToEntity(UserForm userForm) {
        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(String.valueOf(userForm.getPhoneNumber()));
        user.setPasswordHash(userForm.getPassword());
        user.setAddress(userForm.getAddress());
        user.setRole(Role.valueOf(userForm.getRole()));
        user.setActive(true);
        user.setNationality(userForm.getNationality());
        return user;
    }

    private UserForm entityToUserForm(User user) {
        UserForm userForm = new UserForm();
        userForm.setId(String.valueOf(user.getId()));
        userForm.setUsername(user.getUsername());
        userForm.setEmail(user.getEmail());
        userForm.setPhoneNumber(user.getPhoneNumber());
        userForm.setAddress(user.getAddress());
        userForm.setRole(user.getRole().toString());
        userForm.setNationality(user.getNationality());
        userForm.setPassword(user.getPasswordHash());
        userForm.setIsActive(String.valueOf(user.isActive()));
        userForm.setCreatedAt(String.valueOf(user.getCreatedAt()));
        userForm.setUpdatedAt(String.valueOf(user.getUpdatedAt()));
        return userForm;
    }
}
