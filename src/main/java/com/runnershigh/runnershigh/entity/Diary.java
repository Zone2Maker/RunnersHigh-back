package com.runnershigh.runnershigh.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diary {
    private Integer diaryId;
    private Integer userId;
    private String diaryContent;
    private LocalDateTime diaryDate;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
}
