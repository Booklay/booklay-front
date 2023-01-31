package com.nhnacademy.booklay.booklayfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class BooklayFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooklayFrontApplication.class, args);
    }

}
