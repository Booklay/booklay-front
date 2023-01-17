package com.nhnacademy.booklay.booklayfront.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
public class LoginRequest {

    @NotNull
    private final String memberId;

    @NotNull
    private final String password;
}
