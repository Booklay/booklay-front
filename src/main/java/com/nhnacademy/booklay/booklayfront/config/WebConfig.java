package com.nhnacademy.booklay.booklayfront.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.booklayfront.exception.BooklayClientException;
import com.nhnacademy.booklay.booklayfront.exception.BooklayServerException;
import io.micrometer.core.instrument.util.IOUtils;
import java.io.IOException;
import com.nhnacademy.booklay.booklayfront.auth.interceptor.JwtAddInterceptor;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;
import java.time.Duration;
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
                if (response.getStatusCode().is4xxClientError()) {
                    // TODO 4 : shop이나 coupon 서버에서 보내는 에러 코드 (404 Not Found 등등...)에 맞게 에러를 던짐.
                    // TODO 4 : 지금은 없는 정보에 대한 요청이라서 METHOD_NOT_ALLOWED 사용함.
                    if(response.getStatusCode().equals(HttpStatus.METHOD_NOT_ALLOWED)) {

                        // TODO 5 : 응답의 body에서 message 부분을 가져와, 에러 던짐.
                        String body = IOUtils.toString(response.getBody());
                        Map<String, String> map = objectMapper().readValue(body, Map.class);

                        throw new IllegalArgumentException(map.get("message"));
                    }
                    throw new BooklayClientException(response.getStatusCode().name());
                } else if (response.getStatusCode().is5xxServerError()) {
                    throw new BooklayServerException(response.getStatusCode().name());
                }
            }
        };
    }

}
