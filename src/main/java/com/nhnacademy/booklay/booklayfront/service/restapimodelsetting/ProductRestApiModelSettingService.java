package com.nhnacademy.booklay.booklayfront.service.restapimodelsetting;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartObject;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

@Service
@RequiredArgsConstructor
public class ProductRestApiModelSettingService {
    private final RestService restService;
    private final String gatewayIp;

    private static final String STRING_CART_ID_FOR_API = "cartId";
    public void setProductListToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, "/product");
        ApiEntity<PageResponse<ProductAllInOneResponse>> apiEntity =
                restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute("list", apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    public void setProductObjectListToModelByCartId(String cartId, MemberInfo memberInfo, Model model){
        MultiValueMap<String, String> multiValueMap = getMemberInfoMultiValueMap(memberInfo);
        multiValueMap.add(STRING_CART_ID_FOR_API, cartId);
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX);
        ApiEntity<List<CartObject>> apiEntity = restService.get(url, multiValueMap, new ParameterizedTypeReference<>() {});
        List<CartObject> cartObjectList = apiEntity.getBody();
        model.addAttribute(ATTRIBUTE_NAME_CART_OBJECT_LIST, cartObjectList);
    }

    public List<CartObject> setProductObjectListToModelByProductNoList(List<Long> productNoList , Model model){
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("productNoList", productNoList.stream().map(Object::toString).collect(Collectors.toList()));
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "products");
        ApiEntity<List<CartObject>> apiEntity = restService.get(url, multiValueMap, new ParameterizedTypeReference<>() {});
        List<CartObject> cartObjectList = apiEntity.getBody();
        model.addAttribute(ATTRIBUTE_NAME_CART_OBJECT_LIST, cartObjectList);
        return cartObjectList;
    }
}
