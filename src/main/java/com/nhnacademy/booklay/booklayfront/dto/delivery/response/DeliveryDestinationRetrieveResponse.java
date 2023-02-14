package com.nhnacademy.booklay.booklayfront.dto.delivery.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDestinationRetrieveResponse {
    private Long id;
    private Long memberNo;
    private String name;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String addressSubDetail;
    private String receiver;
    private String receiverPhoneNo;
    private Boolean isDefaultDestination;
}
