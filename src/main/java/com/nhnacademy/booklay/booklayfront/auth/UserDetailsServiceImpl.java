package com.nhnacademy.booklay.booklayfront.auth;

import com.nhnacademy.booklay.booklayfront.auth.domain.CustomMember;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberResponse;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String SHOP_PREFIX = "/shop/v1/";
    private final RestTemplate restTemplate;
    private final String gatewayIp;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String url = gatewayIp + SHOP_PREFIX;

        MemberResponse memberResponse =
            restTemplate.getForObject(url + "members/login/?memberId=" + username, MemberResponse.class);

        if (memberResponse == null) {
            throw new UsernameNotFoundException(username);
        }

        return new CustomMember(memberResponse.getEmail(), memberResponse.getPassword(),
                                Collections.singletonList(memberResponse.getAuthority()));
    }
}
