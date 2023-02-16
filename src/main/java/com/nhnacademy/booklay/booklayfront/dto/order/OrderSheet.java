package com.nhnacademy.booklay.booklayfront.dto.order;

import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import java.util.List;

import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponUseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderSheet {

    private List<String> couponCodeList;
    private List<CartDto> cartDtoList;
    private List<OrderProductDto> orderProductDtoList;
    private List<SubscribeDto> subscribeProductList;
    private CouponUseRequest couponUseRequest;
    private Long productPriceSum;
    private Long usingPoint;
    private Long deliveryPrice;
    private Long giftWrappingPrice;
    private Long discountPrice;
    private Long paymentAmount;
    private Long paymentMethod;
    private String orderId;

    private Long memberNo;
    private String sender;
    private String senderPhoneNo;
    private String name;
    private String zipCode;
    private String address;
    private Boolean isDefaultDestination;
    private String receiver;
    private String receiverPhoneNo;
    private String memo;

    @Setter
    private Long orderNo;
    private String orderTitle;
    @Setter
    private Integer pointAccumulate;
}
