package com.runnershigh.runnershigh.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalUser implements UserDetails {
    private Integer userId;
    private String username;    // 닉네임
    @JsonIgnore
    private String password;
    private String email;
    private String profileImgUrl;
    private List<UserRole> userRoles;
    private LocalDateTime createDt;

    private Integer feedCount;
    private Integer crewId;
    private String crewName;

    // 권한 목록 가져오기
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.stream().map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName()))
                .collect(Collectors.toList());
    }
}

