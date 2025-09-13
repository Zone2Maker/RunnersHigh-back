package com.runnershigh.runnershigh.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMessageListReqDto {
    private Integer crewId;
    private Integer userId;
    private Integer size;
    private Integer cursorMessageId;
    // 키셋 페이지네이션
    // 클라이언트가 다음 메시지를 요청할 때 기준점으로 삼을 값이 필요
    // 보통 마지막으로 본 메시지의 ID를 사용
}
