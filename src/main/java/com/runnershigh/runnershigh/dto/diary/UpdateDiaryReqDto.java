package com.runnershigh.runnershigh.dto.diary;

import com.runnershigh.runnershigh.entity.Diary;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateDiaryReqDto {
    private Integer diaryId;
    private Integer userId;
    private String diaryContent;

    public Diary toEntity() {
        return Diary.builder()
                .diaryId(diaryId)
                .diaryContent(diaryContent)
                .build();
    }
}
