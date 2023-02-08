package com.nhnacademy.booklay.booklayfront.dto.search.response;

import lombok.Getter;
import lombok.ToString;

/**
 * 엘라스틱 상품 Docs 필드로 들어갈 작가 정보.
 */

@ToString
@Getter
public class AuthorInfo {

    Long id;
    String name;
}
