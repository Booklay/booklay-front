package com.nhnacademy.booklay.booklayfront.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.booklayfront.exception.BooklayClientException;
import com.nhnacademy.booklay.booklayfront.exception.BooklayServerException;
import java.io.IOException;
import com.nhnacademy.booklay.booklayfront.auth.interceptor.JwtAddInterceptor;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;
import java.time.Duration;
import org.springframework.web.server.MethodNotAllowedException;

@Configuration
public class WebConfig {

    @Bean
    public String gatewayIp(@Value("${booklay.gateway-origin}") String ip) {
        return ip;

    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, RedisTemplate<String, Object> redisTemplate) {

        return builder
                .setReadTimeout(Duration.ofSeconds(5L))
                .setConnectTimeout(Duration.ofSeconds(5L))
                .interceptors(new JwtAddInterceptor(redisTemplate))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .errorHandler(responseErrorHandler())
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public ResponseErrorHandler responseErrorHandler() {
        return new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return (response.getStatusCode().series() == CLIENT_ERROR
                        || response.getStatusCode().series() == SERVER_ERROR);
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().equals(HttpStatus.METHOD_NOT_ALLOWED)) {
                    throw new MethodNotAllowedException(
                        String.valueOf(HttpStatus.METHOD_NOT_ALLOWED), null);
                }

                if (response.getStatusCode().is4xxClientError()) {
                    throw new BooklayClientException(response.getStatusCode().name());
                } else if (response.getStatusCode().is5xxServerError()) {
                    throw new BooklayServerException(response.getStatusCode().name());
                }
            }

        };
    }

}
