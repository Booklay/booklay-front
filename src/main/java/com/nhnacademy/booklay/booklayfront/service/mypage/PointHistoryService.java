package com.nhnacademy.booklay.booklayfront.service.mypage;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.response.PointHistoryRetrieveResponse;

public interface PointHistoryService {
    ApiEntity<PageResponse<PointHistoryRetrieveResponse>> retrievePointHistory(Long memberNo,
                                                                               int page);

    void convertPointCoupon(Long memberNo, Long couponId, Long orderCouponId);
}
