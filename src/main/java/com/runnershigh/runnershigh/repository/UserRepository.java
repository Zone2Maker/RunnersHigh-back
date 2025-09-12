package com.runnershigh.runnershigh.repository;


import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private UserMapper userMapper;

public Optional<User> getUserInfo(Integer userId, String email, String nickname) {
    return userMapper.getUserInfo(userId, email, nickname);
}

public int checkUserExist(String email, String nickname) {
    return userMapper.checkUserExist(email, nickname);
}

public int addUser(User user) {
    return userMapper.addUser(user);
}

public int updateUser(User user) {
    return userMapper.updateUser(user);
}
}

