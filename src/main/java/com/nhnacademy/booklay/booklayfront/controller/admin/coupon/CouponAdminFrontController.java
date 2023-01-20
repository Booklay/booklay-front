package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.*;
import com.nhnacademy.booklay.booklayfront.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon")
public class CouponAdminFrontController {
    private final RestService restService;
    private final String gatewayIp;
    private final ImageUploader imageUploader;
    private static final String RETURN_PAGE = "admin/adminPage";
    private static final String RETURN_PAGE_COUPON_LIST = "redirect:/admin/coupon/list/0";
    private static final String REST_PREFIX = "/coupon/v1";
    private static final String URL_PREFIX = "/admin/coupons";
    private final ObjectMapper objectMapper;

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    @GetMapping("")
    public String adminCouponPage(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/empty");
        return RETURN_PAGE;
    }

    @GetMapping("create")
    public String createCouponForm(Model model) {
        String url = buildString(gatewayIp, REST_PREFIX, "/admin/couponTypes");

        ApiEntity<PageResponse<CouponType>>
            apiEntity = restService.get(url, getDefaultPageMap(0),
            new ParameterizedTypeReference<>() {
            });

        if (!apiEntity.isSuccess()) {
            return ERROR;
        }

        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
        model.addAttribute(TARGET_VIEW, "coupon/createCouponForm");

        return RETURN_PAGE;
    }

    @PostMapping("create")
    public String createCouponPost(
        @ModelAttribute("CouponTemplateAddRequest") CouponAddRequest couponAddRequest,
        @RequestParam("issuanceDeadline")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date date,
        @RequestParam(name = "couponImage", required = false) MultipartFile multipartFile,
        HttpServletRequest request) {
        couponAddRequest.setIssuanceDeadlineAt(
            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        String imagePath = imageUploader.uploadImage(multipartFile, request);
        Map<String, Object> map = objectMapper.convertValue(couponAddRequest, Map.class);

        // FIXME Image 저장 후, 반환 값
        map.put("imageId", 1L);
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX);
        ApiEntity<String> apiEntity = restService.post(url, map, String.class);
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("type/create")
    public String createCouponTypeForm(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/createCouponTypeForm");
        return RETURN_PAGE;
    }

    @PostMapping("type/create")
    public String createCouponPost(@ModelAttribute("CouponTypeAddRequest")
                                   CouponTypeAddRequest couponTypeAddRequest) {
        Map<String, Object> map = objectMapper.convertValue(couponTypeAddRequest, Map.class);
        String url = buildString(gatewayIp, REST_PREFIX, "/admin/couponTypes");
        restService.post(url, map, String.class);
        return "redirect:/admin/coupon/list/type/0";
    }

    @GetMapping("list")
    public String allCouponList0() {
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("list/{pageNum}")
    public String allCouponList(Model model, @PathVariable Integer pageNum) {
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/pages");
        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }

        // FIXME : Boolean is null.
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody().getData());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }

    @GetMapping("list/type/{pageNum}")
    public String allCouponTypeList(Model model, @PathVariable Integer pageNum) {

        String url = buildString(gatewayIp, REST_PREFIX, "/admin/couponTypes");
        ApiEntity<PageResponse<CouponType>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/typeListView");
        return RETURN_PAGE;
    }

    @GetMapping("type/delete/{couponId}")
    public String couponTypeDelete(@PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, "/admin/couponTypes/", couponId);
        restService.delete(url);
        return "redirect:/admin/coupon/list/type/0";
    }

    @GetMapping("list/{memberNo}/{pageNum}")
    public String memberCouponList(Model model, @PathVariable String memberNo,
                                   @PathVariable Integer pageNum) {
        //todo
        String url =
            buildString(gatewayIp, REST_PREFIX, "/members/", memberNo, "/", pageNum.toString());
        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }


    @GetMapping("detail/{couponId}")
    public String viewCoupon(Model model, @PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/", couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        model.addAttribute(TARGET_VIEW, "coupon/detailView");
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
        return RETURN_PAGE;
    }

    @GetMapping("update/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/", couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        apiEntity.getBody().setId(couponId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/couponUpdateForm");
        return RETURN_PAGE;
    }

    @PostMapping("update/{couponId}")
    public String postUpdateCouponForm(@ModelAttribute CouponAddRequest couponAddRequest,
                                       @PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, couponId);
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponAddRequest);
        ApiEntity<String> apiEntity = restService.put(url, map, String.class);
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("delete/{couponId}")
    public String deleteCoupon(@PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/", couponId);
        restService.delete(url);
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("history/{pageNum}")
    public String historyCoupon(Model model, @PathVariable Integer pageNum) {
        String url = buildString(FrontURI.SHOP_URI, REST_PREFIX, "history/", pageNum.toString());
        ApiEntity<CouponHistory> apiEntity =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });
        model.addAttribute(ATTRIBUTE_NAME_HISTORY_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/historyView");
        return RETURN_PAGE;

    }

    @GetMapping("history/{memberNo}/{pageNum}")
    public String memberHistoryCoupon(Model model, @PathVariable String memberNo,
                                      @PathVariable Integer pageNum) {
        String url = buildString(FrontURI.SHOP_URI, REST_PREFIX, "history/", memberNo, "/",
            pageNum.toString());
        ApiEntity<CouponHistory> apiEntity =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });
        model.addAttribute(ATTRIBUTE_NAME_HISTORY_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/historyView");
        return RETURN_PAGE;
    }

    @GetMapping("issue/{pageNum}")
    public String issueCoupon(Model model, @PathVariable Integer pageNum) {
        String url = buildString(FrontURI.SHOP_URI, REST_PREFIX, "issue/", pageNum.toString());
        ApiEntity<CouponIssue> apiEntity =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/issueView");
        return RETURN_PAGE;
    }

    @GetMapping("issue/{memberNo}/{pageNum}")
    public String memberIssueCoupon(Model model, @PathVariable Integer pageNum,
                                    @PathVariable String memberNo) {
        String url = buildString(FrontURI.SHOP_URI, REST_PREFIX, "issue/", memberNo, "/",
            pageNum.toString());
        ApiEntity<CouponIssue> apiEntity =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/issueView");
        return RETURN_PAGE;

    }

    @GetMapping("/issue")
    public String issueCouponForm(Model model) {
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/pages");

        // 쿠폰 모든 종류 찾아서 받아오고, 팝업창에서 선택에서 선택할 수 있게끔..
        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(0), new ParameterizedTypeReference<>() {
            });

        if (!apiEntity.isSuccess()) {
            return ERROR;
        }

        model.addAttribute("couponList", apiEntity.getBody().getData());
        model.addAttribute(TARGET_VIEW, "coupon/issueCouponForm");
        return RETURN_PAGE;
    }

    @PostMapping("/issue")
    public String issueCouponCreate(@Valid @ModelAttribute CouponIssueRequest couponRequest) {
        Map<String, Object> map = objectMapper.convertValue(couponRequest, Map.class);

        String url = buildString(gatewayIp, REST_PREFIX, "/admin/coupons/issue");
        ApiEntity<String> apiEntity = restService.post(url, map, String.class);

        if (!apiEntity.isSuccess()) {
            return ERROR;
        }

        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("/member/issue")
    public String issueCouponToMemberForm(Model model) {
        return RETURN_PAGE;
    }

    @PostMapping("/member/issue")
    public String issueCouponToMember(@Valid @ModelAttribute CouponIssueRequest couponRequest) {
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("/popup/pages/{pageNum}")
    public String couponPopup(@PathVariable int pageNum, Model model) {
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/pages");
        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }

        model.addAttribute("couponList", apiEntity.getBody().getData());

        return "/admin/coupon/couponPopup";
    }



}
