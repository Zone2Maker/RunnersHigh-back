package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    @Autowired
    private UserRepository userRepository;

    //회원 정보 조회
    public ApiRespDto<?> getUserInfo()
}
