package com.runnershigh.runnershigh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;
    private String email;
    @JsonIgnore
    private String password;
    private String nickname;
    private String profileImgUrl;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private List<UserRole> userRoles;
}
