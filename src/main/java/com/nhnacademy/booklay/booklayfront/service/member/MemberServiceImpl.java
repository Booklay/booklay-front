package com.nhnacademy.booklay.booklayfront.service.member;

import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final PasswordEncoder passwordEncoder;
    @Override
    public MemberCreateRequest alterPassword(MemberCreateRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return request;
    }
}
