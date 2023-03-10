package com.nhnacademy.booklay.booklayfront.controller.member;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/")
public class MemberRestController {

  private static final String SHOP_URI_PRE_FIX = "/shop/v1/members";
  private final String gatewayIp;
  private final RestService restService;

  /**
   * ID 증복검사를 위한 호출
   *
   * @return
   */
  @PostMapping("/exist/{memberId}")
  public Boolean existMemberId(@PathVariable String memberId) {
    if (memberId != null) {
      URI uri = URI.create(gatewayIp + SHOP_URI_PRE_FIX + "/exist/" + memberId);

      ApiEntity<Boolean> result = restService.get(uri.toString(), null
          , Boolean.class);

      log.info("결과 출력 " + result.getBody());
      return result.getBody();
    }
    return false;
  }

  /**
   * 닉네임 중복검사를 위한 호출
   * @param nickName
   * @return
   */
  @PostMapping("/exist/nickName/{nickName}")
  public Boolean existNickName(@PathVariable String nickName) {
    if (nickName != null) {
      URI uri = URI.create(gatewayIp + SHOP_URI_PRE_FIX + "/exist/nickName/" + nickName);

      ApiEntity<Boolean> result = restService.get(uri.toString(), null
          , Boolean.class);

      log.info("결과 출력 " + result.getBody());
      return result.getBody();
    }
    return false;
  }

  /**
   * 닉네임 수정 시 중복검사를 위한 호출
   *
   * @param nickName 중복검사 할 닉네임
   * @return
   */
  @PostMapping("/exist/update/nickName/{nickName}")
  public Boolean existNickNameUpdate(MemberInfo memberInfo,
                                     @PathVariable String nickName) {
    if (nickName != null) {
      if (nickName.equals(memberInfo.getNickname())) {
        return false;
      }
      URI uri = URI.create(gatewayIp + SHOP_URI_PRE_FIX + "/exist/nickName/" + nickName);

      ApiEntity<Boolean> result = restService.get(uri.toString(), null
          , Boolean.class);

      log.info("결과 출력 " + result.getBody());
      return result.getBody();
    }
    return false;
  }

  /**
   * 이메일 중복검사를 위한 호출
   *
   * @param eMail 중복검사 할 이메일
   * @return
   */
  @PostMapping("/exist/eMail/{eMail}")
  public Boolean existEMail(@PathVariable String eMail) {
    if (eMail != null) {
      URI uri = URI.create(gatewayIp + SHOP_URI_PRE_FIX + "/exist/eMail/" + eMail);

      ApiEntity<Boolean> result = restService.get(uri.toString(), null
          , Boolean.class);

      log.info("결과 출력 " + result.getBody());
      return result.getBody();
    }
    return false;
  }

  /**
   * 이메일 수정 시 중복검사를 위한 호출
   *
   * @param eMail 중복검사 할 이메일
   * @return
   */
  @PostMapping("/exist/update/eMail/{eMail}")
  public Boolean existEMailUpdate(MemberInfo memberInfo,
                                  @PathVariable String eMail) {
    if (eMail != null) {
      if (eMail.equals(memberInfo.getEmail())) {
        return false;
      }
      URI uri = URI.create(gatewayIp + SHOP_URI_PRE_FIX + "/exist/eMail/" + eMail);

      ApiEntity<Boolean> result = restService.get(uri.toString(), null
          , Boolean.class);

      log.info("결과 출력 " + result.getBody());
      return result.getBody();
    }
    return false;
  }
}
