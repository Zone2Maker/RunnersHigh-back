package com.runnershigh.runnershigh.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveMessageReqDto {
    // STOMP에 Security 적용함으로써 userId는 없어도 됨
    private String message;
    private String messageType;
}
