package com.nhnacademy.booklay.booklayfront.controller.mypage;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ORDER_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.common.PageData;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("member/profile/receipt")
public class ReceiptController {

    private final RestService restService;
    private final String gatewayIp ;
    @GetMapping("list")
    public String orderReceiptListPage(MemberInfo memberInfo, PageData pageData, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "receipt/list");
        ApiEntity<PageResponse<OrderListRetrieveResponse>> orderListApiEntity =
            restService.get(url, getDefaultPageMap(pageData, memberInfo), new ParameterizedTypeReference<>() {});
        model.addAttribute("receiptList", orderListApiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, orderListApiEntity.getBody());
        return "mypage/order/receiptListView";
    }
}
