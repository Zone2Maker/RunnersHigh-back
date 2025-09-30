package com.runnershigh.runnershigh.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveMessageReqDto {
    private String message;
    private String messageType;
}
