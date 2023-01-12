package com.nhnacademy.booklay.booklayfront.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PageResponse<T> {

  private Integer pageNumber;

  private Integer pageSize;

  private Integer totalPages; // 총 페이지 수

  private List<T> data;
}
