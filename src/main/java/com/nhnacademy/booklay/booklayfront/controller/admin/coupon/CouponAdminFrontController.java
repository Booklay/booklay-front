package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.*;
import com.nhnacademy.booklay.booklayfront.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.service.CouponRestApiModelSettingService;
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
    private final CouponRestApiModelSettingService restApiService;
    private final ObjectMapper objectMapper;
    private final ImageUploader imageUploader;
    private final String gatewayIp;

    private static final String RETURN_PAGE = "admin/adminPage";
    private static final String RETURN_PAGE_COUPON_LIST = "redirect:/admin/coupon/list/0";



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
        restApiService.setAllCouponTypeToModel(model);
        model.addAttribute(TARGET_VIEW, "coupon/createCouponForm");
        return RETURN_PAGE;
    }

    @PostMapping("create")
    public String postCreateCoupon(
        @Valid @ModelAttribute("CouponAddRequest") CouponAddRequest couponAddRequest,
        @RequestParam("issuanceDeadline")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date date,
        @RequestParam(name = "couponImage", required = false) MultipartFile multipartFile,
        HttpServletRequest request) {

        couponAddRequest.setIssuanceDeadlineAt(
            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        String imagePath = imageUploader.uploadImage(multipartFile, request);
        Map<String, Object> map = objectMapper.convertValue(couponAddRequest, Map.class);
        map.put("imageId", 1L);

        String url = buildString(gatewayIp, REST_PREFIX, COUPON_URL_PREFIX);

        ApiEntity<String> apiEntity = restService.post(url, map, String.class);


        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("type/create")
    public String createCouponTypeForm(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/createCouponTypeForm");
        return RETURN_PAGE;
    }

    @PostMapping("type/create")
    public String postCreateCoupon(@Valid @ModelAttribute("CouponTypeAddRequest")
                                   CouponTypeAddRequest couponTypeAddRequest) {
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_TYPES_URL_PREFIX);

        Map<String, Object> map = objectMapper.convertValue(couponTypeAddRequest, Map.class);
        restService.post(url, map, String.class);

        return "redirect:/admin/coupon/list/type/0";
    }

    @GetMapping("list")
    public String allCouponList0() {
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("list/{pageNum}")
    public String allCouponList(Model model, @PathVariable Integer pageNum) {
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_URL_PREFIX, "pages");

        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });



        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody().getData());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }

    @GetMapping("list/type/{pageNum}")
    public String allCouponTypeList(Model model, @PathVariable Integer pageNum) {

        String url = buildString(gatewayIp, REST_PREFIX, COUPON_TYPES_URL_PREFIX);
        ApiEntity<PageResponse<CouponType>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });

        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/typeListView");
        return RETURN_PAGE;
    }

    @GetMapping("type/delete/{couponId}")
    public String couponTypeDelete(@PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_TYPES_URL_PREFIX, couponId);
        restService.delete(url);
        return "redirect:/admin/coupon/list/type/0";
    }

    @GetMapping("list/{memberNo}/{pageNum}")
    public String memberCouponList(Model model, @PathVariable String memberNo,
                                   @PathVariable Integer pageNum) {
        String url =
            buildString(gatewayIp, REST_PREFIX, "/members/", memberNo, "/", pageNum.toString());
        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });

        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }


    @GetMapping("detail/{couponId}")
    public String viewCoupon(Model model, @PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);

        model.addAttribute(TARGET_VIEW, "coupon/detailView");
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
        return RETURN_PAGE;
    }

    @GetMapping("update/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_URL_PREFIX, couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);

        apiEntity.getBody().setId(couponId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/couponUpdateForm");
        return RETURN_PAGE;
    }

    @PostMapping("update/{couponId}")
    public String postUpdateCouponForm(@Valid @ModelAttribute CouponAddRequest couponAddRequest,
                                       @PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, couponId);
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponAddRequest);
        restService.put(url, map, String.class);

        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("delete/{couponId}")
    public String deleteCoupon(@PathVariable String couponId) {
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_URL_PREFIX, couponId);
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
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_URL_PREFIX, "/pages");

        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(0), new ParameterizedTypeReference<>() {
            });



        model.addAttribute("couponList", apiEntity.getBody().getData());
        model.addAttribute(TARGET_VIEW, "coupon/issueCouponForm");
        return RETURN_PAGE;
    }

    @PostMapping("/issue")
    public String issueCouponCreate(@Valid @ModelAttribute CouponIssueRequest couponRequest) {
        Map<String, Object> map = objectMapper.convertValue(couponRequest, Map.class);

        String url = buildString(gatewayIp, REST_PREFIX, "/admin/coupons/issue");
        ApiEntity<String> apiEntity = restService.post(url, map, String.class);



        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("/member/issue")
    public String issueCouponToMemberForm(Model model) {

        model.addAttribute(TARGET_VIEW, "coupon/issueCouponToMemberForm");

        return RETURN_PAGE;
    }

    @PostMapping("/member/issue")
    public String issueCouponToMember(@Valid @ModelAttribute CouponIssueRequest couponRequest) {
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("/popup/pages/{pageNum}")
    public String couponPopup(@PathVariable int pageNum, Model model) {
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_URL_PREFIX, "pages");
        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });


        model.addAttribute("couponList", apiEntity.getBody().getData());

        return "/admin/coupon/couponPopup";
    }

    @GetMapping("/member/popup/pages/{pageNum}")
    public String memberPopup(@PathVariable int pageNum, Model model) {
        String url = buildString(gatewayIp, "/shop/v1/admin/members");
        ApiEntity<PageResponse<Coupon>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });



        model.addAttribute("memberList", apiEntity.getBody().getData());

        return "/admin/coupon/couponPopup";
    }

}
