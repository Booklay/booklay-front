package com.nhnacademy.booklay.booklayfront.dto.delivery.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeliveryDestinationCURequest {
    @NotBlank
    private final String name;
    @NotBlank
    private final String zipCode;
    @NotBlank
    private final String address;
    private final String addressDetail;
    private final String addressSubDetail;
    @NotBlank
    private final String receiver;
    @NotBlank
    private final String receiverPhoneNo;
    @NotNull
    private final Boolean isDefaultDestination;
}
