package com.nhnacademy.booklay.booklayfront.dto.member.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberMainRetrieveResponse {
    private Long memberNo;
    private String gender;
    private String memberId;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private String phoneNo;
    private String email;
    private String memberGrade;
    private Integer currentTotalPoint;
}
