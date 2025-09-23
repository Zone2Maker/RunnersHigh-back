package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Diary {
    private Integer diaryId;
    private Integer userId;
    private String diaryContent;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
}
