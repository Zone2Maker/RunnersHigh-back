package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Message {
    private Integer messageId;
    private Integer crewId;
    private Integer userId;
    private String message;
    private String messageType;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
}
