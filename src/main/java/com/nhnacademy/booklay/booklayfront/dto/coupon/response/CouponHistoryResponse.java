package com.nhnacademy.booklay.booklayfront.dto.coupon.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponHistoryResponse {
    private Long id;
    private String code;
    private String name;
    private String memberId;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
}
