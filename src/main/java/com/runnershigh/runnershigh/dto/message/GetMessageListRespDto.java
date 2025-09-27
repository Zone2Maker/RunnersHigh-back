package com.runnershigh.runnershigh.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMessageListRespDto {
    private List<GetMessageRespDto> messages;
    private Long newCursorId;
}
