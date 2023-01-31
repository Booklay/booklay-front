package com.nhnacademy.booklay.booklayfront.service.restapimodelsetting;

import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;

@Service
@RequiredArgsConstructor
public class CategoryRestApiModelSettingService {
    private final RestService restService;
    private final String gatewayIp;

    public void setCategoryListToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, "/admin/categories");
        ApiEntity<PageResponse<CategoryResponse>> apiEntity =
                restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});

        model.addAttribute("list", apiEntity.getBody().getData());
    }
}
