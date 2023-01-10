package com.nhnacademy.booklay.booklayfront.coupon.domain;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:frontUrl.properties")
@Configuration
public class FrontURI {

    @Value("${shopUrl}")
    public String SHOPURI;
}
