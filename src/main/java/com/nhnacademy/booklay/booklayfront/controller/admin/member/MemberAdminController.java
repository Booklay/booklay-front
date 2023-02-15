package com.nhnacademy.booklay.booklayfront.controller.admin.member;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_MEMBER_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberChartRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberGradeChartRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/admin/members")
public class MemberAdminController {
    private final String adminRedirectGatewayPrefix;
    private final String memberRedirectGatewayPrefix;
    private final RestService restService;
    private final ObjectMapper objectMapper;

    public MemberAdminController(String gateway, RestService restService,
                                 ObjectMapper objectMapper) {
        this.adminRedirectGatewayPrefix = gateway + DOMAIN_PREFIX_SHOP + ADMIN_MEMBER_REST_PREFIX;
        this.memberRedirectGatewayPrefix = gateway + DOMAIN_PREFIX_SHOP + "/members";
        this.restService = restService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String retrieveMemberList(@RequestParam(value = "page", defaultValue = "0") int page,
                                     Model model) {
        String query = "?page=" + page;

        URI uri = URI.create(adminRedirectGatewayPrefix + query);

        ApiEntity<PageResponse<MemberRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());

            return "admin/member/memberList";
        } else {
            return "/";
        }
    }

    @GetMapping("/grade/{memberNo}")
    public String retrieveMemberGrade(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @PathVariable Long memberNo,
                                      Model model) {
        String query = "?page=" + page;

        URI uri = URI.create(adminRedirectGatewayPrefix + "/grade/" + memberNo + query);

        ApiEntity<PageResponse<MemberGradeRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());

            return "admin/member/adminMemberGradeList";
        } else {
            return "/";
        }
    }

    @GetMapping("/authority/{memberNo}")
    public String retrieveMemberAuthority(@PathVariable Long memberNo,
                                          Model model) {
        URI uri = URI.create(memberRedirectGatewayPrefix + "/authority/" + memberNo);

        ApiEntity<List<MemberAuthorityRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        model.addAttribute("authorities", response.getBody());
        model.addAttribute("memberNo", memberNo);

        return "mypage/member/memberAuthorityList";
    }

    @GetMapping("/block")
    public String retrieveBlockedMemberList(
        @RequestParam(value = "page", defaultValue = "0") int page,
        Model model) {
        String query = "/block?page=" + page;

        URI uri = URI.create(adminRedirectGatewayPrefix + query);

        ApiEntity<PageResponse<BlockedMemberRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());

            return "admin/member/blockedMemberList";
        } else {
            return "/";
        }
    }

    @GetMapping("/block/history/{memberNo}")
    public String retrieveBlockedMemberHistoryList(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @PathVariable Long memberNo,
        Model model) {

        String query = "/block/detail/" + memberNo +"?page=" + page;

        URI uri = URI.create(adminRedirectGatewayPrefix + query);

        ApiEntity<PageResponse<BlockedMemberRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());

            return "admin/member/blockedMemberHistoryList";
        } else {
            return "/";
        }
    }

    @GetMapping("/dropped")
    public String retrieveDroppedMemberList(
        @RequestParam(value = "page", defaultValue = "0") int page,
        Model model) {
        String query = "/dropped?page=" + page;

        URI uri = URI.create(adminRedirectGatewayPrefix + query);

        ApiEntity<PageResponse<DroppedMemberRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());

            return "admin/member/droppedMemberList";
        } else {
            return "/";
        }
    }

    @GetMapping("/block/{memberNo}")
    public String retrieveMemberBlockForm(@PathVariable Long memberNo, Model model) {
        URI uri = URI.create(adminRedirectGatewayPrefix + "/" + memberNo);

        ApiEntity<MemberRetrieveResponse> response =
            restService.get(uri.toString(), null, MemberRetrieveResponse.class);

        model.addAttribute("member", response.getBody());

        return "admin/member/memberBlockForm";
    }

    @GetMapping("/block/cancel/{blockedMemberDetailId}")
    public String retrieveMemberBlockCancelForm(@PathVariable Long blockedMemberDetailId,
                                                Model model) {
        model.addAttribute("blockedMemberDetailId", blockedMemberDetailId);

        return "admin/member/memberBlockCancelForm";
    }

    @GetMapping("/chart")
    public String retrieveMemberChart(Model model) {
        URI memberUri = URI.create(adminRedirectGatewayPrefix + "/chart");
        URI gradeUri = URI.create(adminRedirectGatewayPrefix + "/grade/chart");

        ApiEntity<MemberChartRetrieveResponse> memberResponse =
            restService.get(memberUri.toString(), null, MemberChartRetrieveResponse.class);

        ApiEntity<MemberGradeChartRetrieveResponse> gradeResponse =
            restService.get(gradeUri.toString(), null, MemberGradeChartRetrieveResponse.class);

        model.addAttribute("counts", memberResponse.getBody());
        model.addAttribute("gradeCounts", gradeResponse.getBody());

        return "admin/member/memberChart";
    }

    @PostMapping("/block/{memberNo}")
    public String memberBlock(@Valid @ModelAttribute MemberBlockRequest request,
                              BindingResult bindingResult,
                              @PathVariable Long memberNo) {
        URI uri = URI.create(adminRedirectGatewayPrefix + "/block/" + memberNo);

        restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
            Void.class);

        return "redirect:/admin/members";
    }

    @GetMapping("/block/cancel/process/{blockedMemberDetailId}")
    public String memberBlockCancel(@PathVariable Long blockedMemberDetailId,
                                    Model model) {
        URI uri = URI.create(adminRedirectGatewayPrefix + "/block/cancel/" + blockedMemberDetailId);

        restService.get(uri.toString(), null, Void.class);

        return "redirect:/admin/members/block";
    }
}
