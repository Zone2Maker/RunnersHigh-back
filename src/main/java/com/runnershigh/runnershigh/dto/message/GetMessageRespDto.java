package com.runnershigh.runnershigh.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    // addCrew, joinCrew 시 발행할 crew
    private Integer crewId;
}