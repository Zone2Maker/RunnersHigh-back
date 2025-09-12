package com.runnershigh.runnershigh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class User {
    private Integer userId;
    private String email;
    @JsonIgnore
    private String password;
    private String nickname;
    private String profileImgUrl;
    private LocalDateTime lastLoginDt;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private List<UserRole> userRoles;
}
