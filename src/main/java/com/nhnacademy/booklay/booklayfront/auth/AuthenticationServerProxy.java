package com.nhnacademy.booklay.booklayfront.auth;

import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 로그인 시도시 인증서버로 요청을 보냅니다.
 * 옳바른 요청을 했을 경우 Authorization 헤더에 access token이 담겨 있습니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationServerProxy {

    private final RestTemplate restTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    private final String gatewayIp;

    private static final String AUTH_PREFIX = "/auth/v1";
    private static final String SHOP_PREFIX = "/shop/v1";
    private static final String UUID_HEADER = "X-User-UUID";

    public JwtInfo sendAuth(String username, String password) {

        String url = gatewayIp + AUTH_PREFIX + "/members/login";

        var loginRequest = new LoginRequest(username, password);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, loginRequest, Void.class);

        List<String> jwtHeaders = response.getHeaders().get(HttpHeaders.AUTHORIZATION);

        String jwt = "";
        String uuid = "";

        if(Objects.nonNull(jwtHeaders) && Objects.nonNull(jwtHeaders.get(0))) {
            jwt = jwtHeaders.get(0).split(" ")[1];
        }

        List<String> uuidHeaders = response.getHeaders().get(UUID_HEADER);

        if(Objects.nonNull(uuidHeaders) && Objects.nonNull(uuidHeaders.get(0))) {
            uuid = uuidHeaders.get(0);
        }

        return JwtInfo.builder()
                .jwt(jwt)
                .uuid(uuid)
                .build();
    }
}
