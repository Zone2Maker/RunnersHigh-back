package com.runnershigh.runnershigh.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GetMessageListRespDto {
    private List<GetMessageRespDto> messages;
    private Integer nextCursorMessageId; // 다음 요청에 사용할 cursorId
}
