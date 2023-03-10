package com.nhnacademy.booklay.booklayfront.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponUsing {
    private Long id; //: 0  // 쿠폰 번호(대리키)
    private String name; //: “string”   // 쿠폰 이름
    private String typeName; //: “string”.   // 쿠폰 종류(코드테이블)
    private String couponCode;
    private Integer amount; //: 0.  // 할인 금액(정액) or 할인율(정률)
    private Integer minimumUseAmount; //: 0   // 최소 사용 금액
    private Integer maximumDiscountAmount; //: 0   // 최대 할인 금액
    private Boolean isLimited;
    private Long categoryNo;
    private Long productNo;
}
