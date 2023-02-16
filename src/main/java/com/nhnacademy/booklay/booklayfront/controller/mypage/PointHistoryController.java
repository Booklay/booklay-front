package com.nhnacademy.booklay.booklayfront.controller.mypage;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.request.PointPresentRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.PointCouponRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.mypage.PointHistoryService;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * @author 양승아
 */
@Slf4j
@Controller
@RequestMapping("/member/profile/point")
public class PointHistoryController {
    private final RestTemplate restTemplate;
    private final RestService restService;
    private final String redirectGatewayPrefix;
    private final String redirectGatewayPrefixCoupon;
    private final PointHistoryService pointHistoryService;

    public PointHistoryController(RestTemplate restTemplate,
                                  PointHistoryService pointHistoryService,
                                  RestService restService,
                                  String gateway) {
        this.restTemplate = restTemplate;
        this.restService = restService;
        this.pointHistoryService = pointHistoryService;
        this.redirectGatewayPrefix = gateway + DOMAIN_PREFIX_SHOP + "/point";
        this.redirectGatewayPrefixCoupon = gateway + DOMAIN_PREFIX_COUPON + "/members";
    }

    @GetMapping("{memberNo}")
    public String adminRetrievePointList(@RequestParam(value = "page", defaultValue = "0") int page,
                                         @PathVariable Long memberNo,
                                         Model model) {

        ApiEntity<PageResponse<PointHistoryRetrieveResponse>> response =
            pointHistoryService.retrievePointHistory(memberNo, page);

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());

            return "admin/member/adminMemberPointList";
        } else {
            return "/";
        }
    }

    @GetMapping(value = {"/", ""})
    public String memberRetrievePointList(
        @RequestParam(value = "page", defaultValue = "0") int page,
        MemberInfo memberInfo,
        Model model) {
        ApiEntity<PageResponse<PointHistoryRetrieveResponse>> response =
            pointHistoryService.retrievePointHistory(memberInfo.getMemberNo(), page);

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());

            return "mypage/member/memberPointList";
        } else {
            return "/";
        }
    }

    @GetMapping("/present")
    public String retrievePointPresentForm(MemberInfo memberInfo,
                                           Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + "/total/" + memberInfo.getMemberNo());

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, uri);

        ResponseEntity<TotalPointRetrieveResponse> response =
            restTemplate.exchange(requestEntity, TotalPointRetrieveResponse.class);

        model.addAttribute("totalPoint", response.getBody());

        return "mypage/member/memberPointPresent";
    }

    /**
     * 포인트 쿠폰 리스트 조회 메소드
     *
     * @param memberInfo
     * @param model
     * @return
     */
    @GetMapping("/coupon")
    public String retrievePointCouponList(MemberInfo memberInfo,
                                          Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(
            redirectGatewayPrefixCoupon + "/" + memberInfo.getMemberNo() + "/coupons/point");

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, uri);

        ResponseEntity<PageResponse<PointCouponRetrieveResponse>> response =
            restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<>() {
                });

        List<PointCouponRetrieveResponse> list =
            Objects.requireNonNull(response.getBody()).getData();

        model.addAttribute("list", list);
        model.addAttribute("memberNo", memberInfo.getMemberNo());
        model.addAttribute("targetUrl", "member/memberPointCouponList");

        return "mypage/member/memberPointCouponList";
    }

    /**
     * 회원 간 포인트 선물하는 메소드
     *
     * @param pointPresentRequest 포인트 선물 정보 dto
     * @param bindingResult
     * @param memberInfo
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/present")
    public String pointPresent(@Valid @ModelAttribute PointPresentRequest pointPresentRequest,
                               BindingResult bindingResult,
                               MemberInfo memberInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = URI.create(redirectGatewayPrefix + "/present/" + memberInfo.getMemberNo());

        RequestEntity<String> requestEntity =
            new RequestEntity<>(objectMapper.writeValueAsString(pointPresentRequest), headers,
                HttpMethod.POST, uri);

        restTemplate.exchange(requestEntity, Void.class);

        return "redirect:/member/profile/point/";
    }

    /**
     * 포인트 쿠폰을 포인트로 조회할 건지 묻는 페이지 조회
     *
     * @param memberInfo
     * @param couponId
     * @return
     */
    @GetMapping("/coupon/view/{couponId}/{orderCouponId}")
    public String pointCouponConvertView(MemberInfo memberInfo,
                                         @PathVariable Long couponId,
                                         @PathVariable Long orderCouponId,
                                         Model model) {

        model.addAttribute("couponId", couponId);
        model.addAttribute("orderCouponId", orderCouponId);
        model.addAttribute("memberNo", memberInfo.getMemberNo());

        return "mypage/member/pointCouponConvertForm";
    }

    @GetMapping("/coupon/{couponId}/{orderCouponId}")
    public String convertPointCoupon(MemberInfo memberInfo,
                                     @PathVariable Long couponId,
                                     @PathVariable Long orderCouponId,
                                     Model model) {
        pointHistoryService.convertPointCoupon(memberInfo.getMemberNo(), couponId, orderCouponId);

        return "complete";
    }
}
