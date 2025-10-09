package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.user.UserProfileDto;
import com.runnershigh.runnershigh.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> getUserInfo(Integer userId, String email, String nickname);
    Optional<UserProfileDto> getUserProfileById(Integer userId);
    int checkUserExist (String email, String nickname);
    int addUser (User user);
    int updateUser(User user);
    int deleteUser(Integer userId);
}
