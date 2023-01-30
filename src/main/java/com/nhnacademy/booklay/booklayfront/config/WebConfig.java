package com.nhnacademy.booklay.booklayfront.config;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.booklayfront.exception.BooklayClientException;
import com.nhnacademy.booklay.booklayfront.exception.BooklayServerException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Bean
    public String gatewayIp(@Value("${booklay.gateway-origin}") String ip) {
        return ip;
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
                if (response.getStatusCode().is4xxClientError()) {
                    throw new BooklayClientException(response.getStatusCode().name());
                } else if (response.getStatusCode().is5xxServerError()) {
                    throw new BooklayServerException(response.getStatusCode().name());
                }
            }
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .errorHandler(responseErrorHandler())
            .build();
    }
}
