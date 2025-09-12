package com.runnershigh.runnershigh.dto.User;

import com.runnershigh.runnershigh.entity.User;

public class AddUserReqDto {

    private String email;
    private String password;
    private String nickname;
    private String name;
    private String profileImgUrl;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
//                .lastLoginDt(null) // 가입 시점에는 null 인데 NN 이니 제외
                .build();
    }
}
