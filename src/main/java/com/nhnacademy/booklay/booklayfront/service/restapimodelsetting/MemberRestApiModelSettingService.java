package com.nhnacademy.booklay.booklayfront.service.restapimodelsetting;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
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
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, "/admin/members");
        ApiEntity<PageResponse<MemberRetrieveResponse>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});

        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
        model.addAttribute("list", apiEntity.getBody().getData());
    }
}
