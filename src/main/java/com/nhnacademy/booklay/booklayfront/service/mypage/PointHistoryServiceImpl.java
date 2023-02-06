package com.nhnacademy.booklay.booklayfront.service.mypage;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class PointHistoryServiceImpl implements PointHistoryService {
    private final RestService restService;
    private final String redirectGatewayPrefix;
    private final String redirectGatewayPrefixCoupon;

    public PointHistoryServiceImpl(RestService restService, String gateway) {
        this.restService = restService;
        this.redirectGatewayPrefix = gateway + DOMAIN_PREFIX_SHOP + "/point";
        this.redirectGatewayPrefixCoupon = gateway + DOMAIN_PREFIX_COUPON + "/members";
    }

    @Override
    public ApiEntity<PageResponse<PointHistoryRetrieveResponse>> retrievePointHistory(
        Long memberNo, int page) {
        String query = "?page=" + page;

        URI uri = URI.create(redirectGatewayPrefix + "/" + memberNo + query);
        return restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
        });
    }
}
