package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductResponse {

    private  Long id;

    private  Long objectFileId;

    private  String title;

    private  LocalDateTime createdAt;

    private  Long price;

    private  Long pointRate;

    private  String shortDescription;

    private  String longDescription;

    private  boolean selling;

    private  boolean pointMethod;

    private  boolean deleted;
    
}
