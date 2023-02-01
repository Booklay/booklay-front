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
    @NotBlank
    private final String addressDetail;
    @NotNull
    private final Boolean isDefaultDestination;
}
