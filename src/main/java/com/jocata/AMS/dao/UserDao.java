package com.jocata.AMS.dao;

import com.jocata.AMS.entity.User;

import java.util.List;

public interface UserDao {
    public User createUser(User user);
    public User getUserByEmail(String email);
    public User getUserById(Integer id);
    public List<User> getAllUsers();
}
