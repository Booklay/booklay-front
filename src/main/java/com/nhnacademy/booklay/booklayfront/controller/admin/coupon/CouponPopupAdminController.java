package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.COUPON_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;

import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.Coupon;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons/popup/")
public class CouponPopupAdminController {

    private final RestService restService;
    private final String gatewayIp;

    private static final String RESOURCE_BASE = "/admin/coupon/";

    @GetMapping("/coupon/pages/{pageNum}")
    public String couponPopup(@PathVariable int pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX, COUPON_REST_PREFIX, "pages");
        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });

        model.addAttribute("couponList", apiEntity.getBody().getData());

        return RESOURCE_BASE + "couponPopup";
    }

    @GetMapping("/member/pages/{pageNum}")
    public String memberPopup(@PathVariable int pageNum, Model model) {
        String url = buildString(gatewayIp, "/shop/v1/admin/members");
        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });

        model.addAttribute("memberList", apiEntity.getBody().getData());

        return RESOURCE_BASE + "memberPopup";
    }

    @GetMapping("/category/pages/{pageNum}")
    public String categoryPopup(@PathVariable int pageNum, Model model) {
        String url = buildString(gatewayIp, "/shop/v1/admin/categories");
        ApiEntity<PageResponse<CategoryResponse>> apiEntity =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });

        model.addAttribute("categories", apiEntity.getBody().getData());

        return RESOURCE_BASE + "categoryPopup";
    }

    @GetMapping("/product/pages/{pageNum}")
    public String productPopup(@PathVariable int pageNum, Model model) {
        String url = buildString(gatewayIp, "/shop/v1/product");
        ApiEntity<PageResponse<RetrieveProductResponse>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });

        model.addAttribute("productList", apiEntity.getBody().getData());

        return RESOURCE_BASE + "productPopup";
    }
}
