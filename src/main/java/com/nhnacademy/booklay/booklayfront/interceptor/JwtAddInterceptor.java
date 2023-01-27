package com.nhnacademy.booklay.booklayfront.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAddInterceptor implements ClientHttpRequestInterceptor{

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * {@link ClientHttpRequestInterceptor} 인터페이스 추상 메서드를 재정의하여 서버로 요청을 보내기 전 토큰 정보가 있다면 주입 후 요청을 보냅니다.
     *
     * @param httpRequest - the request, containing method, URI, and headers
     * @param body        - the body of the request
     * @param execution   -  the request execution
     * @return 클라이언트 요청에 대한 응답 인터페이스 {@link ClientHttpResponse}
     * @throws IOException - {@link ClientHttpRequestExecution} execute 메서드 실행 도중 시 입출력 예외 발생 시 처리하기 위한 객체
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        log.info("path = {}", httpRequest.getURI().getPath());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!this.needJwt(httpRequest.getURI().getPath())) {
            return execution.execute(httpRequest, body);
        }

        log.info("RestTemplate Interceptor");

        String uuid = (String) authentication.getPrincipal();
        Optional<String> token = Optional.ofNullable(((String) redisTemplate.opsForHash().get(uuid, "TOKEN")));

        if (token.isEmpty()) {
            return execution.execute(httpRequest, body);
        }

        log.info("token = {}", token.get());

        httpRequest.getHeaders().setBearerAuth(token.get());

        return execution.execute(httpRequest, body);}

    /**
     * {@link HttpRequest} 로 받은 요청에 대해 토큰 기반 인증이 필요한지 검사합니다.
     *
     * @param uri - 토큰 기반 인증 검사가 필요한 요청(request) URI
     * @return 토큰 기반 인증이 필요한 URI 검사 여부
     */
    public boolean needJwt(String uri) {
        return !(uri.contains("login") || uri.contains("signup"));
    }
}
