package com.runnershigh.runnershigh.dto.user;

import com.runnershigh.runnershigh.entity.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class JoinReqDto {
    private String email;     //이메일
    private String nickname;  //닉네임
    private String password;  //비밀번호

    // DTO를 User 엔티티로 변환하는 메서드
    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password)) // 비밀번호는 암호화하여 저장
                .nickname(this.nickname)
                .build();
    }


}
