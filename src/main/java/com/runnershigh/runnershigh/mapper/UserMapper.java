package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.user.UserProfileDto;
import com.runnershigh.runnershigh.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> getUserInfo(
            @Param("userId") Integer userId,
            @Param("email") String email,
            @Param("nickname") String nickname
    );
    Optional<UserProfileDto> getUserProfileById(Integer userId);
    int checkUserExist (
            @Param("email") String email,
            @Param("nickname") String nickname
    );
    int addUser (User user);
    int updateUser(User user);
}
