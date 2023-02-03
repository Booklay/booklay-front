package com.nhnacademy.booklay.booklayfront.service.restapimodelsetting;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

@Service
@RequiredArgsConstructor
public class MemberRestApiModelSettingService {
    private final RestService restService;
    private final String gatewayIp;

    public void setMemberListToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, "/admin/members");
        ApiEntity<PageResponse<MemberRetrieveResponse>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});

        model.addAttribute("list", apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    public void setMemberPointToModelByMemberNo(String memberId, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, "/point/total/", memberId);
        ApiEntity<PointHistoryRetrieveResponse> apiEntity = restService.get(url, null, PointHistoryRetrieveResponse.class);
        model.addAttribute("point", apiEntity.getBody().getPoint());
    }
}
