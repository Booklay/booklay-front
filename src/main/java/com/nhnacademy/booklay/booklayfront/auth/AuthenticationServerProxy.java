package com.nhnacademy.booklay.booklayfront.auth;

import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationServerProxy {

    private final RestTemplate restTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    private final String gatewayIp;

    private static final String PREFIX = "/auth/v1";
    private static final String UUID_HEADER = "X-User-UUID";

    public JwtInfo sendAuth(String username, String password) {

        String url = gatewayIp + PREFIX + "/members/login";

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

            redisTemplate.opsForHash()
                    .put(uuid, "TOKEN", jwt);
        }

        return JwtInfo.builder()
                .jwt(jwt)
                .uuid(uuid)
                .build();
    }
}
