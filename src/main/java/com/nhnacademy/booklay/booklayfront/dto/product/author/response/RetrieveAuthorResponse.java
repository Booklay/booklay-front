package com.nhnacademy.booklay.booklayfront.dto.product.author.response;

import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberForAuthorResponse;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RetrieveAuthorResponse {

  @NotNull
  Long authorNo;

  @NotNull
  String name;

  MemberForAuthorResponse member;
}
