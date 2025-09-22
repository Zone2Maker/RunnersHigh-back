package com.runnershigh.runnershigh.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateLastReadMessageReqDto {
    private Long lastReadMessageId;
}
