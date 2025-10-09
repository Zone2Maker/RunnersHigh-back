package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.entity.UserRole;
import com.runnershigh.runnershigh.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleRepository {

    @Autowired
    private UserRoleMapper userRoleMapper;
    public int addUserRole(UserRole userRole){
        return userRoleMapper.addUserRole(userRole);
    }
}
