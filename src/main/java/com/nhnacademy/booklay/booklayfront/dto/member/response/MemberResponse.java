package com.nhnacademy.booklay.booklayfront.dto.member.response;

import com.nhnacademy.booklay.booklayfront.auth.constant.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private String userId;

    private String password;

    private Roles authority;

    private String email;
}
