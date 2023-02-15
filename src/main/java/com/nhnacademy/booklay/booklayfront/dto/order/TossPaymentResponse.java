package com.nhnacademy.booklay.booklayfront.dto.order;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class TossPaymentResponse {
    private String mId;
    private String version;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String currency;
    private String method;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime requestedAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime approvedAt;
    private Boolean useEscrow;
    private Boolean cultureExpense;
    private TossPaymentCard cart;
    private String virtualAccount;
    private String transfer;
    private String mobilePhone;
    private String giftCertificate;
    private String foreignEasyPay;
    private String cashReceipt;
    private String discount;
    private String cancels;
    private String secret;
    private String type;
    private String easyPay;
    private String country;
    private String failure;
    private Integer totalAmount;
    private Integer balanceAmount;
    private Integer suppliedAmount;
    private Integer vat;
    private Integer taxFreeAmount;
    private Integer taxExemptionAmount;
}
