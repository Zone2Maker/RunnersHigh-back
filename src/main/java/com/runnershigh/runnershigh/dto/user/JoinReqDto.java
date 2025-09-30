package com.runnershigh.runnershigh.dto.user;

import com.runnershigh.runnershigh.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class JoinReqDto {
    private String email;
    private String password;

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder, String nickname) {
        return User.builder()
                .email(this.email)
                .password(bCryptPasswordEncoder.encode(this.password))
                .nickname(nickname)
                .build();
    }

}
