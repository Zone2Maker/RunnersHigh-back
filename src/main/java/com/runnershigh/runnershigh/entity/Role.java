package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {
    private Integer roleId;
    private String roleName;
    private String roleNameKor;
}
