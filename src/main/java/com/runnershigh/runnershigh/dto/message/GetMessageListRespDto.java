package com.runnershigh.runnershigh.dto.message;

import com.runnershigh.runnershigh.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetMessageListRespDto {
    // Message 필드
    private Integer messageId;
    private Integer userId;
    private String message;
    private String messageType;
    private LocalDateTime createDt;

    // User 필드
    private String nickname;
    private String profileImgUrl;
}