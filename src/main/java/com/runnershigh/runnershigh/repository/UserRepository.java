package com.runnershigh.runnershigh.repository;


import com.runnershigh.runnershigh.dto.user.UserProfileDto;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.mapper.UserMapper;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
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

    public Optional<UserProfileDto> getUserProfileById(Integer userId) {
        return userMapper.getUserProfileById(userId);
    }

    public int checkUserExist(String email, String nickname) {
        return userMapper.checkUserExist(email, nickname);
    }

    public Optional<User> addUser(User user) {
        int result = userMapper.addUser(user);
        if(result == 0){
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    public int deleteUser(Integer userId) {
        return userMapper.deleteUser(userId);
    }
}

