package com.nhnacademy.booklay.booklayfront.service.member;

import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberCreateRequest;

public interface MemberService {
    MemberCreateRequest alterPassword(MemberCreateRequest memberCreateRequest);
}
