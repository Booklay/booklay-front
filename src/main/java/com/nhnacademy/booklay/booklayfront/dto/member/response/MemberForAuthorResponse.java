package com.nhnacademy.booklay.booklayfront.dto.member.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForAuthorResponse {
  Long memberNo;
  String memberId;

  public MemberForAuthorResponse(Long memberNo, String memberId) {
    this.memberNo = memberNo;
    this.memberId = memberId;
  }
}
