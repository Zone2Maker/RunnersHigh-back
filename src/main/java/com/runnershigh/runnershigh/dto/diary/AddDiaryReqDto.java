package com.runnershigh.runnershigh.dto.diary;

import com.runnershigh.runnershigh.entity.Diary;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddDiaryReqDto {
    Integer userId;
    String diaryContent;
    String date;

    public Diary toEntity() {
        return Diary.builder()
                .userId(userId)
                .diaryContent(diaryContent)
                .createDt(LocalDate.parse(date).atStartOfDay())
                .build();
    }
}
