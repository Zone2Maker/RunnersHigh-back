package com.runnershigh.runnershigh.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckUserExistReqDto {
    private Integer userId;
    private String email;
    private String nickname;
}
