package com.nhnacademy.booklay.booklayfront.config;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.booklayfront.auth.interceptor.JwtAddInterceptor;
import com.nhnacademy.booklay.booklayfront.exception.BooklayClientException;
import com.nhnacademy.booklay.booklayfront.exception.BooklayServerException;
import com.nhnacademy.booklay.booklayfront.exception.CouponZoneException;
import io.micrometer.core.instrument.util.IOUtils;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.server.MethodNotAllowedException;

@Configuration
public class WebConfig {

  @Bean
  @Primary
  public String gatewayIp(@Value("${booklay.gateway-origin}") String ip) {
    return ip;
  }

  @Bean
  public String domainIp(@Value("${booklay.domain-origin}") String ip) {
    return ip;
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {

    return builder
        .setReadTimeout(Duration.ofSeconds(5L))
        .setConnectTimeout(Duration.ofSeconds(5L))
        .interceptors(new JwtAddInterceptor())
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
  public FilterRegistrationBean<MultipartFilter> multipartFilter() {
    FilterRegistrationBean<MultipartFilter> registrationBean
        = new FilterRegistrationBean<>();

    registrationBean.setFilter(new MultipartFilter());
    registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);

    return registrationBean;
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

        if (response.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)) {
          String messageString = IOUtils.toString(response.getBody());
          Map<String, String> map = objectMapper().readValue(messageString, Map.class);

          String message = map.get("message");
          throw new CouponZoneException(message);
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
