package com.runnershigh.runnershigh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserRole {
    private Integer userRoleId;
    private Integer userId;
    private Integer roleId;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private Role role;
}
