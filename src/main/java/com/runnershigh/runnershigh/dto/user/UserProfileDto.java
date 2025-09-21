package com.runnershigh.runnershigh.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.runnershigh.runnershigh.entity.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UserProfileDto {
    private Integer userId;
    private Integer feedCount;
    private Integer crewId;
    private String crewName;
}
