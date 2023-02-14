package com.nhnacademy.booklay.booklayfront.dto.order;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryDetailDto {

    private Integer deliveryStatusNo;
    private String deliveryStatus;
    private String zipCode;
    private String address;
    private String sender;
    private String senderPhoneNumber;
    private String receiver;
    private String receiverPhoneNumber;
    private String memo;
    private String invoiceNo;
    private LocalDateTime deliveryStartAt;
    private LocalDateTime completedAt;
}
