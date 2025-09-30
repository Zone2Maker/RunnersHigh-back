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
    private Long messageId;
    private Integer userId;
    private String message;
    private String messageType;
    private LocalDateTime createDt;
    private String nickname;
    private String profileImgUrl;
    private Integer crewId;
}