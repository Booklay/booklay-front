package com.nhnacademy.booklay.booklayfront.auth.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class JwtInfo {

    private final String jwt;

    private final String uuid;
}
