package com.nhnacademy.booklay.booklayfront.controller.member;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.member.MemberService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

    private final String redirectGatewayPrefix;
    private final String gatewayIp;
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;

    public MemberController(RestService restService, String gateway,
                            MemberService memberService, ObjectMapper objectMapper) {
        this.gatewayIp = gateway;
        this.restService = restService;
        this.objectMapper = objectMapper;
        this.memberService = memberService;
        redirectGatewayPrefix = gateway + DOMAIN_PREFIX_SHOP + "/members";
    }


    @PostMapping("/register")
    public String createMember(@Valid @ModelAttribute MemberCreateRequest memberCreateRequest,
                               BindingResult bindingResult) {
        URI uri = URI.create(redirectGatewayPrefix);

        restService.post(uri.toString(),
            objectMapper.convertValue(memberService.alterPassword(memberCreateRequest), Map.class),
            Void.class);
        //TODO 2: 에러처리

        return "redirect:/";
    }

    @GetMapping("/login")
    public String retrieveLoginForm() {
        return "member/loginForm";
    }

    @GetMapping(value = {"", "/", "/profile"})
    public String mypageIndex(Model model, MemberInfo memberInfo) {
        URI memberUri = URI.create(redirectGatewayPrefix + "/" + memberInfo.getMemberNo());

        ApiEntity<MemberRetrieveResponse> memberResponse =
            restService.get(memberUri.toString(), null, MemberRetrieveResponse.class);

        if (memberResponse.isSuccess()) {
            model.addAttribute("member", memberResponse.getBody());
        }

        URI wishlistUri =
            URI.create(gatewayIp + DOMAIN_PREFIX_SHOP + "/mypage/product/index/wishlist/"
                + memberInfo.getMemberNo());

        ApiEntity<List<RetrieveProductResponse>> wishlistResponse =
            restService.get(wishlistUri.toString(),
                null, new ParameterizedTypeReference<List<RetrieveProductResponse>>() {
                });

        if (wishlistResponse.isSuccess()) {
            model.addAttribute("wishlist", wishlistResponse.getBody());
        }

        return "mypage/profile/main";
    }


    @GetMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String retrieveCreateMemberForm(Model model) {
        return "member/register";
    }

    @GetMapping("/detail")
    public String retrieveMemberDetail(Model model, MemberInfo memberInfo) {
        URI uri = URI.create(redirectGatewayPrefix + "/" + memberInfo.getMemberNo());

        ApiEntity<MemberRetrieveResponse> response =
            restService.get(uri.toString(), null, MemberRetrieveResponse.class);

        model.addAttribute("member", response.getBody());
        model.addAttribute("memberNo", memberInfo.getMemberNo());

        log.info("member id = {}", memberInfo.getMemberId());
        return "mypage/member/memberDetail";
    }

    @GetMapping("/profile/grade")
    public String retrieveMemberGrade(@RequestParam(value = "page", defaultValue = "0") int page,
                                      MemberInfo memberInfo,
                                      Model model) {
        String query = "?page=" + page;

        URI uri = URI.create(redirectGatewayPrefix + "/grade/" + memberInfo.getMemberNo() + query);

        ApiEntity<PageResponse<MemberGradeRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());

            return "mypage/member/memberGradeList";
        } else {
            return "/";
        }
    }

    @GetMapping("/authority")
    public String retrieveMemberAuthority(MemberInfo memberInfo, Model model) {
        URI uri = URI.create(redirectGatewayPrefix + "/authority/" + memberInfo.getMemberNo());

        ApiEntity<List<MemberAuthorityRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        model.addAttribute("authorities", response.getBody());
        model.addAttribute("memberNo", memberInfo.getMemberNo());

        return "mypage/member/memberAuthorityList";
    }

    @GetMapping("/update")
    public String retrieveMemberUpdateForm(MemberInfo memberInfo, Model model) {
        String query = "/" + memberInfo.getMemberNo();

        URI uri = URI.create(redirectGatewayPrefix + query);

        ApiEntity<MemberRetrieveResponse> response =
            restService.get(uri.toString(), null, MemberRetrieveResponse.class);

        if (response.isSuccess()) {
            model.addAttribute("member", response.getBody());
            model.addAttribute("memberNo", memberInfo.getMemberNo());

            return "mypage/member/memberUpdateForm";
        } else {
            return "/";
        }
    }

    @PostMapping("/update")
    public String updateMember(MemberInfo memberInfo,
                               @Valid @ModelAttribute MemberUpdateRequest request,
                               BindingResult bindingResult) {

        URI uri = URI.create(redirectGatewayPrefix + "/" + memberInfo.getMemberNo());

        restService.put(uri.toString(), objectMapper.convertValue(request, Map.class),
            Void.class);

        return "redirect:/member/" + memberInfo.getMemberNo();
    }

    @GetMapping("/drop")
    public String retrieveDeleteMemberForm(MemberInfo memberInfo, Model model) {
        model.addAttribute("memberNo", memberInfo.getMemberNo());

        return "mypage/member/memberDeleteForm";
    }

    @GetMapping("/drop/process")
    public String deleteMember(MemberInfo memberInfo) {
        URI uri = URI.create(redirectGatewayPrefix + "/" + memberInfo.getMemberNo());

        restService.delete(uri.toString());
        
        return "redirect:/";
    }
}
