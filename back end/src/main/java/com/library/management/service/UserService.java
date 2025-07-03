package com.library.management.service;

import com.library.management.dao.UserDao;
import com.library.management.model.User;

import java.util.Optional;

public class UserService {
    private UserDao userDao = new UserDao();

    public User registerUser(String username, String password, String fullName, User.Role role) {
        if (userDao.findUserByUsername(username).isPresent()) {
            System.out.println("Username already exists.");
            return null;
        }
        User newUser = new User(username, password, fullName, role);
        return userDao.addUser(newUser);
    }

    public Optional<User> loginUser(String username, String password) {
        Optional<User> userOptional = userDao.findUserByUsername(username);
        if (userOptional.isPresent()) {
            if (userDao.checkPassword(username, password)) {
                return userOptional;
            }
        }
        return Optional.empty();
    }

    public Optional<User> findUserById(int userId) {
        return userDao.findUserById(userId);
    }
} 