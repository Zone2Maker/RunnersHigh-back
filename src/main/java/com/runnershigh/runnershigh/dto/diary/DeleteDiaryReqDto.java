package com.runnershigh.runnershigh.dto.diary;

import lombok.Data;

@Data
public class DeleteDiaryReqDto {
    private Integer diaryId;
    private Integer userId;
}
