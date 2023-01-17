package com.nhnacademy.booklay.booklayfront.controller.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class LoginRequest {

    @NotNull
    private String userId;

    @NotNull
    private String password;
}
