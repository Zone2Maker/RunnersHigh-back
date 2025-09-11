package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class User {
    private Integer userId;
    private String email;
    private String password;
    private String nickname;
    private String profileImgUrl;
    private Integer crewId;
    private LocalDateTime last_login_dt;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private List<UserRole> userRoles;
}
