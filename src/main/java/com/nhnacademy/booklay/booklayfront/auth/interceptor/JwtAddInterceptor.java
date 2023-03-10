package com.nhnacademy.booklay.booklayfront.auth.interceptor;

import com.nhnacademy.booklay.booklayfront.auth.CustomMember;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

/**
 * 사용자가 요청할때마다 헤더에 JWT를 달아줍니다.
 *
 * @author 조현진
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAddInterceptor implements ClientHttpRequestInterceptor {

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

        String path = httpRequest.getURI().getPath();

        log.info("path = {}", path);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!this.needJwt(httpRequest.getURI().getPath())) {
            return execution.execute(httpRequest, body);
        }

        if (isAnonymousOrOauth2Token(authentication)) {
            return execution.execute(httpRequest, body);
        }

        log.info("RestTemplate Interceptor 동작. 요청 헤더에 JWT를 추가합니다. path = {}", path);

        CustomMember customMember = (CustomMember) authentication.getPrincipal();

        if (Objects.isNull(customMember)) {
            return execution.execute(httpRequest, body);
        }

        String jwt = customMember.getAccessToken();

        log.info("token = {}", jwt);

        httpRequest.getHeaders().setBearerAuth(jwt);

        return execution.execute(httpRequest, body);
    }

    /**
     * {@link HttpRequest} 로 받은 요청에 대해 토큰 기반 인증이 필요한지 검사합니다.
     *
     * @param uri - 토큰 기반 인증 검사가 필요한 요청(request) URI
     * @return 토큰 기반 인증이 필요한 URI 검사 여부
     */
    private boolean needJwt(String uri) {
        return !(uri.contains("login") || uri.contains("signup"));
    }

    private boolean isAnonymousOrOauth2Token(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(
            a -> a.getAuthority().equals("ROLE_ANONYMOUS") || authentication instanceof OAuth2AuthenticationToken);
    }
}
