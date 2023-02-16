package com.nhnacademy.booklay.booklayfront.dto.search.response;

import java.util.List;
import lombok.Getter;


/**
 * Booklay 서비스에서 요청에 대한 응답 결과를 페이지 정보와 함께 제공하는 클래스입니다.
 * json으로 반환 받기 때문에 shop 프로젝트에서 사용하는 것을 그대로 가져오게 되면 직렬화 오류가 발생함
 * <p>
 * * @param <T> 페이징 처리가 되는 요소의 응답 정보 타입
 */
@Getter
public class SearchPageResponse<T>{

  private String searchKeywords;
  private Long totalHits;
  private Integer pageNumber;
  private String averageScore;
  private Integer pageSize;
  private Integer totalPages; // 총 페이지 수
  private List<T> data;

}
