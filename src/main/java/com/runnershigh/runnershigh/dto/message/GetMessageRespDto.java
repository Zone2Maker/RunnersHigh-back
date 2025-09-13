package com.runnershigh.runnershigh.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class GetMessageRespDto {
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