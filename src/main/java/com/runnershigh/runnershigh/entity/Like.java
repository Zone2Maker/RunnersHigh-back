package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Like {
    private Integer likeId;
    private Integer feedId;
    private Integer userId;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
}
