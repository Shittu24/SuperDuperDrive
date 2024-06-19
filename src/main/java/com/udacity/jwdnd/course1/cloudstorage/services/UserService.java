package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HashService hashService;

    public Integer getUserId(String username) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            return user.getUserId();
        }
        return null;
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.findByUsername(username) == null;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        user.setSalt(encodedSalt);
        user.setPassword(hashedPassword);

        return userMapper.insert(user);
    }

    public User getUser(String username) {
        return userMapper.findByUsername(username);
    }
}
