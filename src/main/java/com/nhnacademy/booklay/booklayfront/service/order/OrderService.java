package com.nhnacademy.booklay.booklayfront.service.order;

import com.nhnacademy.booklay.booklayfront.dto.order.request.TossPaymentConfirmDto;
import com.nhnacademy.booklay.booklayfront.dto.order.response.OrderSheetSaveResponse;

public interface OrderService {
    void tossPayment(TossPaymentConfirmDto tossPaymentConfirmDto)
        throws InterruptedException;

    OrderSheetSaveResponse saveOrder(TossPaymentConfirmDto tossPaymentConfirmDto);
}
