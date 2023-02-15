package com.nhnacademy.booklay.booklayfront.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderReceipt {
    private Long orderNo;
    private Long orderStatusNo;
    private String orderStatus;
    private LocalDateTime orderedAt;
    private Long productPriceSum;
    private Long deliveryPrice;
    private Long discountPrice;
    private Long pointUsePrice;
    private Long paymentPrice;
    private Long paymentMethod;
    private Long giftWrappingPrice;
    List<DeliveryDetailDto> deliveryDetailDtoList;
    List<OrderProductDto> orderProductDtoList;
}
