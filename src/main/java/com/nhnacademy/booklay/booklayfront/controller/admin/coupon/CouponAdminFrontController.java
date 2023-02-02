package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponAddRequest;
import com.nhnacademy.booklay.booklayfront.dto.storage.response.ObjectFileResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

@SuppressWarnings("unchecked")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
@Slf4j
public class CouponAdminFrontController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;
    private final RestTemplate restTemplate;

    private final CouponRestApiModelSettingService couponRestApiModelSettingService;

    private static final String RETURN_PAGE_COUPON_LIST = "redirect:/admin/coupons/list";
    

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    /**
     * coupon index
     *
     */
    @GetMapping
    public String adminCouponPage(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/empty");
        return RETURN_ADMIN_PAGE;
    }

    /**
     * 쿠폰 생성 폼.
     *
     */
    @GetMapping("/create-form")
    public String createCouponForm(Model model) {
        couponRestApiModelSettingService.setAllCouponTypeToModel(model);

        model.addAttribute(TARGET_VIEW, "coupon/createCouponForm");
        return RETURN_ADMIN_PAGE;
    }

    /**
     * 쿠폰 생성.
     * @param couponAddRequest 쿠폰 생성에 필요한 요청.
     */
    @PostMapping("/create-form")
    public String postCreateCoupon(@Valid @ModelAttribute CouponAddRequest couponAddRequest,
                                   @RequestParam(name = "image", required = false) MultipartFile image) throws IOException {

        Map<String, Object> map = objectMapper.convertValue(couponAddRequest, Map.class);

        // 이미지 저장
        if (!Objects.isNull(image) && !image.isEmpty()) {
            URI storageUrl = URI.create(gatewayIp + DOMAIN_PREFIX_SHOP + "/storage");
            ByteArrayResource contentsAsResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            MultipartBodyBuilder resource = new MultipartBodyBuilder();
            resource.part("file",
                    contentsAsResource,
                    MediaType.valueOf(Objects.requireNonNull(image.getContentType())));

            MultiValueMap<String, HttpEntity<?>> multipartBody = resource.build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody,
                    headers);

            ResponseEntity<ObjectFileResponse> responseEntity = restTemplate.postForEntity(storageUrl, httpEntity,
                    ObjectFileResponse.class);
            map.put("fileId", responseEntity.getBody().getId());
        }
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX);

        restService.post(url, map, String.class);

        return RETURN_PAGE_COUPON_LIST;
    }

    /**
     * 생성된 쿠폰 리스트 조회.
     *
     */
    @GetMapping("/list")
    public String allCouponList(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                Model model) {
        couponRestApiModelSettingService.setCouponListToModelByPage(pageNum, model);

        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");

        return RETURN_ADMIN_PAGE;
    }

    @GetMapping("list/{memberNo}/{pageNum}")
    public String memberCouponList(Model model, @PathVariable String memberNo,
                                   @PathVariable Integer pageNum) {
        couponRestApiModelSettingService.setCouponListToModelByPageAndMemberNo(pageNum, memberNo, model);
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_ADMIN_PAGE;
    }

    /**
     * 쿠폰 상세 조회.
     * @param couponId 상세 조회 하려는 쿠폰의 id
     */
    @GetMapping("/detail/{couponId}")
    public String viewCoupon(Model model, @PathVariable String couponId) {
        couponRestApiModelSettingService.setCouponDetailToModelByCouponId(couponId, model);
        model.addAttribute(TARGET_VIEW, "coupon/detailView");

        return RETURN_ADMIN_PAGE;
    }

    /**
     * 쿠폰 수정 폼.
     * @param couponId 수정하려는 쿠폰의 id
     */
    @GetMapping("update/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId) {
        couponRestApiModelSettingService.setCouponDetailToModelByCouponId(couponId, model);
        couponRestApiModelSettingService.setAllCouponTypeToModel(model);

        model.addAttribute(TARGET_VIEW, "coupon/couponUpdateForm");
        return RETURN_ADMIN_PAGE;
    }

    /**
     * 쿠폰 수정.
     * @param couponRequest 수정 내용
     * @param couponId 수정하려는 쿠폰의 id
     */
    @PostMapping("update/{couponId}")
    public String postUpdateCouponForm(@Valid @ModelAttribute CouponAddRequest couponRequest,
                                       @PathVariable String couponId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, couponId);
        Map<String, Object> map = objectMapper.convertValue(couponRequest, Map.class);

        restService.put(url, map, String.class);

        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("delete/{couponId}")
    public String deleteCoupon(@PathVariable String couponId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, couponId);
        restService.delete(url);
        return RETURN_PAGE_COUPON_LIST;
    }
}
