package com.nhnacademy.booklay.booklayfront.auth;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginRequest {

    @NotNull
    private final String memberId;

    @NotNull
    private final String password;
}
