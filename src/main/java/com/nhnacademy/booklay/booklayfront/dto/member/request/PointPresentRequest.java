package com.nhnacademy.booklay.booklayfront.dto.member.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PointPresentRequest {
    @NotBlank
    private String targetMemberId;
    @Min(1)
    @NotNull
    private Integer targetPoint;
}
