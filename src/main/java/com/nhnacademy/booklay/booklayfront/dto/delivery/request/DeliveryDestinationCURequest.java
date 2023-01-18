package com.nhnacademy.booklay.booklayfront.dto.delivery.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDestinationCURequest {
    @NotBlank
    private String name;
    @NotBlank
    private String zipCode;
    @NotBlank
    private String address;
    @NotBlank
    private Boolean isDefaultDestination;
}
