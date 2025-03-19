package com.jocata.AMS.dao.impl;

import com.jocata.AMS.config.HibernateConfig;
import com.jocata.AMS.dao.UserDao;
import com.jocata.AMS.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private final HibernateConfig hibernateConfig;

    public UserDaoImpl(HibernateConfig hibernateConfig) {
        this.hibernateConfig = hibernateConfig;
    }

    @Override
    public User createUser(User user) {
        return hibernateConfig.saveOrUpdateEntity(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return hibernateConfig.findEntityByCriteria(User.class, "email", email);
    }

    @Override
    public User getUserById(Integer id) {
        return hibernateConfig.findEntityById(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return hibernateConfig.loadEntitiesByCriteria(User.class);
    }
}
