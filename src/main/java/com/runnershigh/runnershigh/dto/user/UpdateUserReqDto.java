package com.runnershigh.runnershigh.dto.user;

import com.runnershigh.runnershigh.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserReqDto {
    private Integer userId;
    private String nickname;
    private String profileImgUrl;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .build();
    }
}
