package com.nhnacademy.booklay.booklayfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableRedisHttpSession
@EnableDiscoveryClient
public class BooklayFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooklayFrontApplication.class, args);
    }

}
