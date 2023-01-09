package com.nhnacademy.booklay.booklayfront.coupon.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Coupon {
        private Long couponId; //: 0  // 쿠폰 번호(대리키)
        private String name; //: “string”   // 쿠폰 이름
        private String typeName; //: “string”.   // 쿠폰 종류(코드테이블)
        private Long amount; //: 0.  // 할인 금액(정액) or 할인율(정율)
        private Long minimumUseAmount; //: 0   // 최소 사용 금액
        private Long maximumDiscountAmount; //: 0   // 최대 할인 금액
}
