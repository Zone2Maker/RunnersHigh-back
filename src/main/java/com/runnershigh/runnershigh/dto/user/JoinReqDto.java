package com.runnershigh.runnershigh.dto.user;

import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.random.RandomNicknameGenerator;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Data
public class JoinReqDto {
    private String email;     //이메일
//    private String nickname;  //닉네임 - 랜덤
    private String password;  //비밀번호

    // DTO를 User 엔티티로 변환하는 메서드
    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password)) // 비밀번호는 암호화하여 저장
                .nickname(RandomNicknameGenerator.generate())
                .profileImgUrl("https://example.com/default_profile.png")
                .build();
    }


}
