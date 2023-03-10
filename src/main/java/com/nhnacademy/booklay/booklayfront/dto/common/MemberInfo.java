package com.nhnacademy.booklay.booklayfront.dto.common;

import com.nhnacademy.booklay.booklayfront.auth.constant.Roles;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor
@ToString
public class MemberInfo {

    private Long memberNo;
    private String gender;
    private String memberId;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private String phoneNo;
    private String email;
    private String memberGrade;
    private Roles authority;

    public MemberInfo (MemberRetrieveResponse memberRetrieveResponse) {
        this.memberNo = memberRetrieveResponse.getMemberNo();
        this.gender = memberRetrieveResponse.getGender();
        this.memberId = memberRetrieveResponse.getMemberId();
        this.nickname = memberRetrieveResponse.getNickname();
        this.name = memberRetrieveResponse.getName();
        this.birthday = memberRetrieveResponse.getBirthday();
        this.phoneNo = memberRetrieveResponse.getPhoneNo();
        this.email = memberRetrieveResponse.getEmail();
        this.memberGrade = memberRetrieveResponse.getMemberGrade();
        this.authority = Roles.valueOf(memberRetrieveResponse.getAuthority());
    }

}
