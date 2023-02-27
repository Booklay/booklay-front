package com.nhnacademy.booklay.booklayfront.service.member;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_MEMBER_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberMainRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    private final String adminRedirectGatewayPrefix;
    private final String memberRedirectGatewayPrefix;
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(String gateway, RestService restService,
                             ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
        this.adminRedirectGatewayPrefix = gateway + DOMAIN_PREFIX_SHOP + ADMIN_MEMBER_REST_PREFIX;
        this.memberRedirectGatewayPrefix = gateway + DOMAIN_PREFIX_SHOP + "/members";
        this.restService = restService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String alterPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public ApiEntity<List<MemberAuthorityRetrieveResponse>> retrieveMemberAuthority(Long memberNo) {
        URI uri = URI.create(memberRedirectGatewayPrefix + "/authority/" + memberNo);

        return restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
        });
    }

    @Override
    public ApiEntity<MemberMainRetrieveResponse> retrieveMemberMain(Long memberNo) {
        URI uri = URI.create(memberRedirectGatewayPrefix + "/main/" + memberNo);
        return restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
        });
    }
}
