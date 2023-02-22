package com.nhnacademy.booklay.booklayfront.service.mypage;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PointHistoryServiceImpl implements PointHistoryService {
    private final RestService restService;
    private final String redirectGatewayPrefixPoint;
    private final String redirectGatewayPrefixCoupon;
    private final ObjectMapper objectMapper;


    public PointHistoryServiceImpl(RestService restService, String gateway,
                                   ObjectMapper objectMapper) {
        this.restService = restService;
        this.redirectGatewayPrefixPoint = gateway + DOMAIN_PREFIX_SHOP + "/point";
        this.redirectGatewayPrefixCoupon = gateway + DOMAIN_PREFIX_COUPON;
        this.objectMapper = objectMapper;

    }

    @Override
    public ApiEntity<PageResponse<PointHistoryRetrieveResponse>> retrievePointHistory(
        Long memberNo, int page) {
        String query = "?page=" + page;

        URI uri = URI.create(redirectGatewayPrefixPoint + "/" + memberNo + query);
        return restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
        });
    }

    /**
     * 포인트 쿠폰을 포인트로 전환하는 메소드
     * <p>
     * 쿠폰 정보 불러와서
     * 포인트 적립하고
     * 쿠폰 사용 여부 표시
     *
     * @param memberNo 쿠폰 사용하는 사용자 번호
     * @param couponId 사용할 포인트 쿠폰 ID
     */
    @Override
    public void convertPointCoupon(Long memberNo, Long couponId, Long orderCouponId) {
        URI couponUri = URI.create(redirectGatewayPrefixCoupon + "/admin/coupons/" + couponId);
        ApiEntity<CouponDetailRetrieveResponse> response =
            restService.get(couponUri
                .toString(), null, CouponDetailRetrieveResponse.class);

        Integer point = response.getBody().getAmount();

        URI pointUri = URI.create(redirectGatewayPrefixPoint);

        restService.post(pointUri.toString(),
            objectMapper.convertValue(new PointHistoryCreateRequest(memberNo, point, "포인트 쿠폰 전환"),
                Map.class), Void.class);

        URI couponDeleteUri = URI.create(
            redirectGatewayPrefixCoupon + "/members/" + memberNo + "/coupons/point/" +
                orderCouponId);
        restService.post(couponDeleteUri.toString(), null, Void.class);
    }

    @Override
    public boolean checkUsedPointCoupon(Long memberNo, Long orderCouponId) {
        URI uri = URI.create(
            redirectGatewayPrefixCoupon + "/members/" + memberNo + "/coupons/point/used/" +
                orderCouponId);

        ApiEntity<Boolean> response =
            restService.get(uri.toString(), null, Boolean.class);

        return response.getBody();
    }
}
