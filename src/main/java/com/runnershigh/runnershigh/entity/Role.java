package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role {
    private Integer roleId;
    private String roleName;
    private String roleNameKor;
}
