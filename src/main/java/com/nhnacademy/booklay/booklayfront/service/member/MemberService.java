package com.nhnacademy.booklay.booklayfront.service.member;

import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberMainRetrieveResponse;
import java.util.List;

public interface MemberService {
    MemberCreateRequest alterPassword(MemberCreateRequest memberCreateRequest);

    ApiEntity<List<MemberAuthorityRetrieveResponse>> retrieveMemberAuthority(Long memberNo);

    ApiEntity<MemberMainRetrieveResponse> retrieveMemberMain(Long memberNo);
}
