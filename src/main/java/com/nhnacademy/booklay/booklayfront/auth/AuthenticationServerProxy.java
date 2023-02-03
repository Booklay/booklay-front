package com.nhnacademy.booklay.booklayfront.auth;

import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    private static final String GITHUB_PREFIX_ID = "GIT_";

    public JwtInfo attemptFormAuthentication(String username, String password) {

        String url = gatewayIp + AUTH_PREFIX + "/members/login";

        var loginRequest = new LoginRequest(username, password);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, loginRequest, Void.class);

        List<String> accessTokenHeaders = response.getHeaders().get(HttpHeaders.AUTHORIZATION);
        List<String> refreshTokenHeaders = response.getHeaders().get("Refresh_Token");

        String accessToken = "";
        String refreshToken = "";
        String uuid = "";

        if (Objects.nonNull(accessTokenHeaders) && Objects.nonNull(accessTokenHeaders.get(0)) &&
            Objects.nonNull(refreshTokenHeaders)) {
            accessToken = accessTokenHeaders.get(0).split(" ")[1];
            refreshToken = refreshTokenHeaders.get(0);
        }

        List<String> uuidHeaders = response.getHeaders().get(UUID_HEADER);

        if (Objects.nonNull(uuidHeaders) && Objects.nonNull(uuidHeaders.get(0))) {
            uuid = uuidHeaders.get(0);
        }

        return JwtInfo.builder()
                      .accessToken(accessToken)
                      .refreshToken(refreshToken)
                      .uuid(uuid)
                      .build();
    }

    public JwtInfo attemptGithubOauth2Authentication(String email, String gitId) {
        String url = gatewayIp + AUTH_PREFIX + "/members/login/oauth2/github";

        OAuth2LoginRequest oAuth2LoginRequest =
            new OAuth2LoginRequest(email, GITHUB_PREFIX_ID + gitId);

        ResponseEntity<Void> response =
            restTemplate.postForEntity(url, oAuth2LoginRequest, Void.class);

        List<String> accessTokenHeaders = response.getHeaders().get(HttpHeaders.AUTHORIZATION);
        List<String> refreshTokenHeaders = response.getHeaders().get("Refresh_Token");

        String accessToken = "";
        String refreshToken = "";
        String uuid = "";

        if (Objects.nonNull(accessTokenHeaders) && Objects.nonNull(accessTokenHeaders.get(0)) &&
            Objects.nonNull(refreshTokenHeaders)) {
            accessToken = accessTokenHeaders.get(0).split(" ")[1];
            refreshToken = refreshTokenHeaders.get(0);
        }

        List<String> uuidHeaders = response.getHeaders().get(UUID_HEADER);

        if (Objects.nonNull(uuidHeaders) && Objects.nonNull(uuidHeaders.get(0))) {
            uuid = uuidHeaders.get(0);
        }

        return JwtInfo.builder()
                      .accessToken(accessToken)
                      .refreshToken(refreshToken)
                      .uuid(uuid)
                      .build();
    }
}
