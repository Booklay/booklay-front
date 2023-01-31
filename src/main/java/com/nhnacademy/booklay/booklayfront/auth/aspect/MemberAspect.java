package com.nhnacademy.booklay.booklayfront.auth.aspect;

import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MemberAspect {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TokenUtils tokenUtils;
    private final RestService restService;

    @Around("@within(controller) && execution(* *.*(.., com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo, ..))")
    public Object injectMember(ProceedingJoinPoint pjp, Controller controller) throws Throwable {
        log.info("Method: {}", pjp.getSignature().getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAnonymous(authentication)) {
            return pjp.proceed();
        }

        String uuid = (String) authentication.getPrincipal();

        String jwt = (String) redisTemplate.opsForHash().get(uuid, "TOKEN");

        if (Objects.isNull(jwt)) {
            return pjp.proceed();
        }
        // TODO: 이메일로 유저정보 가져오기

        String email = tokenUtils.getEmail(jwt);
        String url = tokenUtils.getShopUrl() + email;


        ApiEntity<MemberRetrieveResponse> memberRetrieveResponse =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });

        MemberInfo memberInfo = new MemberInfo(memberRetrieveResponse.getBody());

        Object[] args = Arrays.stream(pjp.getArgs())
                              .map(arg -> {
                                  if (arg instanceof MemberInfo) {
                                      arg = memberInfo;
                                  }
                                  return arg;
                              }).toArray();


        return pjp.proceed(args);
    }

    private boolean isAnonymous(Authentication authentication) {
        return (Objects.nonNull(authentication) &&
            authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
    }
}
