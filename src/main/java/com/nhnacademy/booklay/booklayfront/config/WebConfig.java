package com.nhnacademy.booklay.booklayfront.config;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.rmi.ServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

@Configuration
public class WebConfig {


    @Bean
    public String gatewayIp(@Value("${booklay.gateway-origin}") String ip) {
        return ip;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ResponseErrorHandler responseErrorHandler() {
        return new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return (
                    response.getStatusCode().series() == CLIENT_ERROR
                        || response.getStatusCode().series() == SERVER_ERROR);
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
                } else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
                    String reasonPhrase = response.getStatusCode().getReasonPhrase();
                    throw new IllegalArgumentException(reasonPhrase);
                } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                    //throw new HttpStatusNotFoundException();
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
