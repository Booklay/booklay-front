package com.nhnacademy.booklay.booklayfront.service.member;

import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberCreateRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    @Override
    public MemberCreateRequest alterPassword(MemberCreateRequest request) {
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        return request;
    }
}
