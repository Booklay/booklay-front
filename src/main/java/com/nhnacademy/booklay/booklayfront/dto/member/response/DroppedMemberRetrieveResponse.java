package com.nhnacademy.booklay.booklayfront.dto.member.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DroppedMemberRetrieveResponse {
    private String memberId;
    private LocalDateTime deletedAt;
}
