package com.nhnacademy.booklay.booklayfront.auth.aspect;

import com.nhnacademy.booklay.booklayfront.auth.CustomMember;
import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import java.util.Arrays;
import java.util.Objects;

/**
 * 로그인한 멤버의 정보를 받아오기 위한 AOP.
 * Controller 클래스에서 메소드 파라미터에 MemberInfo를 넣으면 주입됩니다.
 *
 * @author 조현진
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MemberAspect {
    private final TokenUtils tokenUtils;
    private final RestService restService;

    /**
     * Member 정보를 주입합니다.
     *
     * @param pjp 메소드 원본의 정보를 갖는 객체입니다.
     * @return 메소드 정보
     * @throws Throwable 메소드 실행 시 발생할 수 있는 예외
     */
    @Around("@within(controller) && execution(* *.*(.., com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo, ..))")
    public Object injectMember(ProceedingJoinPoint pjp, Controller controller) throws Throwable {
        log.info("Method: {}", pjp.getSignature().getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || isAnonymous(authentication)) {
            return pjp.proceed();
        }


        CustomMember principal = (CustomMember) authentication.getPrincipal();

        String jwt = principal.getAccessToken();

        if (Objects.isNull(jwt)) {
            return pjp.proceed();
        }

        String email = principal.getUsername();
        String url = tokenUtils.getShopUrl() + email;


        ApiEntity<MemberRetrieveResponse> memberRetrieveResponse =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });

        MemberInfo memberInfo = new MemberInfo(memberRetrieveResponse.getBody());

        Object[] args = Arrays.stream(pjp.getArgs()).map(arg -> {
            if (arg instanceof MemberInfo) {
                arg = memberInfo;
            }
            if (arg instanceof Model){
                ((Model) arg).addAttribute("memberInfo", memberInfo);
            }
            return arg;
        }).toArray();


        return pjp.proceed(args);
    }

    @Around("@within(restController) && execution(* *.*(.., com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo, ..))")
    public Object injectMember(ProceedingJoinPoint pjp, RestController restController) throws Throwable {
        log.info("Method: {}", pjp.getSignature().getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || isAnonymous(authentication)) {
            return pjp.proceed();
        }


        CustomMember principal = (CustomMember) authentication.getPrincipal();

        String jwt = principal.getAccessToken();

        if (Objects.isNull(jwt)) {
            return pjp.proceed();
        }

        String email = principal.getUsername();
        String url = tokenUtils.getShopUrl() + email;


        ApiEntity<MemberRetrieveResponse> memberRetrieveResponse =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });

        MemberInfo memberInfo = new MemberInfo(memberRetrieveResponse.getBody());

        Object[] args = Arrays.stream(pjp.getArgs()).map(arg -> {
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
