package com.nhnacademy.booklay.booklayfront.dto.product.author.response;

import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberForAuthorResponse;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RetrieveAuthorResponse {

  private Long authorNo;

  private String name;

  private MemberForAuthorResponse member;

}
