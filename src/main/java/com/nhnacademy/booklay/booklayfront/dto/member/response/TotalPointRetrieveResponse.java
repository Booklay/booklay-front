package com.nhnacademy.booklay.booklayfront.dto.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalPointRetrieveResponse {
    private Long member;
    private Integer totalPoint;
}
