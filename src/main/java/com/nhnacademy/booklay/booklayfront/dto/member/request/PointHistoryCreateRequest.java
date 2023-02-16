package com.nhnacademy.booklay.booklayfront.dto.member.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryCreateRequest {
    private Long memberNo;
    private Integer point;
    private String updatedDetail;
}
