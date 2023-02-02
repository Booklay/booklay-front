package com.nhnacademy.booklay.booklayfront.controller.admin.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
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
    private final String redirectGatewayPrefix;
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final static String ADMINPAGE = "admin/adminPage";

    public MemberAdminController(String gateway, RestService restService,
                                 ObjectMapper objectMapper) {
        this.redirectGatewayPrefix = gateway + "/shop/v1" + "/admin/members";
        this.restService = restService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String retrieveMemberList(@RequestParam(value = "page", defaultValue = "0") int page,
                                     Model model) {
        String query = "?page=" + page;

        URI uri = URI.create(redirectGatewayPrefix + query);

        ApiEntity<PageResponse<MemberRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());
            model.addAttribute("targetUrl", "member/memberList");

            return ADMINPAGE;
        } else {
            return "/";
        }
    }

    @GetMapping("/block")
    public String retrieveBlockedMemberList(
        @RequestParam(value = "page", defaultValue = "0") int page,
        Model model) {
        String query = "/block?page=" + page;

        URI uri = URI.create(redirectGatewayPrefix + query);

        ApiEntity<PageResponse<BlockedMemberRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());
            model.addAttribute("targetUrl", "member/blockedMemberList");

            return ADMINPAGE;
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

        URI uri = URI.create(redirectGatewayPrefix + query);

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

        URI uri = URI.create(redirectGatewayPrefix + query);

        ApiEntity<PageResponse<DroppedMemberRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            model.addAttribute("list", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("currentPage", response.getBody().getPageNumber());
            model.addAttribute("targetUrl", "member/droppedMemberList");

            return ADMINPAGE;
        } else {
            return "/";
        }
    }

    @GetMapping("/block/{memberNo}")
    public String retrieveMemberBlockForm(@PathVariable Long memberNo, Model model) {
        URI uri = URI.create(redirectGatewayPrefix + "/" + memberNo);

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

    @GetMapping("/detail/{memberNo}")
    public String retrieveMemberDetail(@PathVariable Long memberNo, Model model) {
        model.addAttribute("memberNo", memberNo);
        model.addAttribute("targetUrl", "member/adminMemberDetailSelect");

        return ADMINPAGE;
//        return "admin/member/adminMemberDetailSelect";
    }

    @PostMapping("/block/{memberNo}")
    public String memberBlock(@Valid @ModelAttribute MemberBlockRequest request,
                              BindingResult bindingResult,
                              @PathVariable Long memberNo,
                              Model model) {
        URI uri = URI.create(redirectGatewayPrefix + "/block/" + memberNo);

        restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
            Void.class);

        return "redirect:/admin/members";
    }

    @GetMapping("/block/cancel/process/{blockedMemberDetailId}")
    public String memberBlockCancel(@PathVariable Long blockedMemberDetailId,
                                    Model model) {
        URI uri = URI.create(redirectGatewayPrefix + "/block/cancel/" + blockedMemberDetailId);

        restService.get(uri.toString(), null, Void.class);

        return "redirect:/admin/members/block";
    }
}
