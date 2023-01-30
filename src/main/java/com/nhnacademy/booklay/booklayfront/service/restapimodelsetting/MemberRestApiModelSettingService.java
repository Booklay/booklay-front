package com.nhnacademy.booklay.booklayfront.service.restapimodelsetting;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_MEMBER_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;

import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.Coupon;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class MemberRestApiModelSettingService {
    private final RestService restService;
    private final String gatewayIp;

    public void setMemberListToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ADMIN_MEMBER_REST_PREFIX);
        ApiEntity<PageResponse<Coupon>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute("memberList", apiEntity.getBody().getData());
    }
}
