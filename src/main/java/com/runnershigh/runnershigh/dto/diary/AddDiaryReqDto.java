package com.runnershigh.runnershigh.dto.diary;

import com.runnershigh.runnershigh.entity.Diary;
import lombok.Data;

@Data
public class AddDiaryReqDto {
    Integer userId;
    String diaryContent;

    public Diary toEntity() {
        return Diary.builder()
                .userId(userId)
                .diaryContent(diaryContent)
                .build();
    }
}
