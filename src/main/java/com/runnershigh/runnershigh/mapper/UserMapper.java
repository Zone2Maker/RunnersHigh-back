package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    //회원정보 조회
    //userId, email, nickname 중 하나로 회원 정보와 권한을 함께 조회
    Optional<User> getUserInfo(
            @Param("userId") Integer userId,
            @Param("email") String email,
            @Param("nickname") String nickname
    );
    //return : User 객체

    //이메일 or 닉네임으로 존재 유무 확인
    //존재하면 1, 없으면 0
    public int checkUserExist (
            @Param("email") String email,
            @Param("nickname") String nickname
    );



    //신규 회원 추가
    public int addUser (User user);

    public int updateUser(User user);

}
