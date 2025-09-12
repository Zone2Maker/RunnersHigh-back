package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int addUserRole(UserRole userRole);
    int updateUserRole(UserRole userRole);

}
