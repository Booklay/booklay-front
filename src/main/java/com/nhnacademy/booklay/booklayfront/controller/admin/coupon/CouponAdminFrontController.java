package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_COUPON_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.RETURN_ADMIN_PAGE;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponAddRequest;
import com.nhnacademy.booklay.booklayfront.service.ObjectFileService;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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

    private final ObjectFileService fileService;
    private final CouponRestApiModelSettingService couponRestApiModelSettingService;

    private static final String ADMIN_COUPON_RESOURCE = "admin/coupon/";
    private static final String RETURN_PAGE_COUPON_LIST = "redirect:/admin/coupons/list";

    /**
     * admin/coupons 인덱스 페이지는 쿠폰 조회 페이지.
     */
    @GetMapping
    public String adminCouponPage() {
        return RETURN_PAGE_COUPON_LIST;
    }

    /**
     * 쿠폰 생성 폼.
     */
    @GetMapping("/create-form")
    public String createCouponForm(Model model) {
        couponRestApiModelSettingService.setAllCouponTypeToModel(model);

        return ADMIN_COUPON_RESOURCE + "createCouponForm";
    }

    /**
     * 쿠폰 생성.
     * @param couponAddRequest 쿠폰 생성에 필요한 요청.
     */
    @PostMapping("/create-form")
    public String postCreateCoupon(@Valid @ModelAttribute CouponAddRequest couponAddRequest,
                                   @RequestParam(name = "image", required = false) MultipartFile image) throws IOException {

        Map<String, Object> map = objectMapper.convertValue(couponAddRequest, Map.class);

        if (!Objects.isNull(image) && !image.isEmpty()) {
            Long fileId = fileService.uploadImage(image);
            map.put("fileId", fileId);
        }

        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX);
        restService.post(url, map, String.class);

        return RETURN_PAGE_COUPON_LIST;
    }

    /**
     * 생성된 쿠폰 리스트 조회.
     */
    @GetMapping("/list")
    public String allCouponList(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                Model model) {
        couponRestApiModelSettingService.setCouponListToModelByPage(pageNum, model);

        return ADMIN_COUPON_RESOURCE + "listView";
    }

    /**
     * 특정 사용자의 쿠폰 조회
     * @param memberNo 특정 사용자의 Id
     */
    @GetMapping("list/{memberNo}")
    public String memberCouponList(Model model, @PathVariable String memberNo,
                                   @RequestParam(value = "page", defaultValue = "0") int pageNum) {
        couponRestApiModelSettingService.setCouponListToModelByPageAndMemberNo(pageNum, memberNo, model);

        return RETURN_ADMIN_PAGE;
    }

    /**
     * 쿠폰 상세 조회.
     * @param couponId 상세 조회 하려는 쿠폰의 id
     */
    @GetMapping("/detail/{couponId}")
    public String viewCoupon(Model model, @PathVariable String couponId) {
        couponRestApiModelSettingService.setCouponDetailToModelByCouponId(couponId, model);

        return ADMIN_COUPON_RESOURCE + "detailView";
    }

    /**
     * 쿠폰 수정 폼.
     * @param couponId 수정하려는 쿠폰의 id
     */
    @GetMapping("update-form/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId) {
        couponRestApiModelSettingService.setCouponDetailToModelByCouponId(couponId, model);
        couponRestApiModelSettingService.setAllCouponTypeToModel(model);

        return ADMIN_COUPON_RESOURCE + "couponUpdateForm";
    }

    /**
     * 쿠폰의 내용을 수정합니다.
     * 발급된 쿠폰이 있으면 수정할 수 없습니다.
     *
     * @param couponRequest 수정 내용
     * @param couponId 수정하려는 쿠폰의 id
     */
    @PostMapping("update-form/{couponId}")
    public String postUpdateCouponForm(@Valid @ModelAttribute CouponAddRequest couponRequest,
                                       @PathVariable String couponId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, couponId);
        Map<String, Object> map = objectMapper.convertValue(couponRequest, Map.class);

        restService.put(url, map, String.class);

        return RETURN_PAGE_COUPON_LIST;
    }

    /**
     * 쿠폰의 이미지 수정.
     * @param couponId 이미지를 수정하려는 쿠폰의 id
     */
    @GetMapping("update-image/{couponId}")
    public String updateCouponImageForm(Model model, @PathVariable String couponId) {
        couponRestApiModelSettingService.setCouponDetailToModelByCouponId(couponId, model);

        return ADMIN_COUPON_RESOURCE + "couponImageUpdateForm";
    }

    /**
     * 쿠폰의 이미지를 변경합니다.
     * 이미지 등록을 하지 않고 submit 했을 때, 이미지가 바뀌지 않습니다.
     * @param couponId 이미지를 바꿀 쿠폰의 Id
     * @param image 저장할 이미지
     */
    @PostMapping("update-image/{couponId}")
    public String updateCouponImage(@PathVariable Long couponId,
                                    @RequestParam(name = "image", required = false) MultipartFile image)
        throws IOException {

        if (!Objects.isNull(image) && !image.isEmpty()) {
            Long fileId = fileService.uploadImage(image);

            String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "image/", couponId.toString(), "/", fileId.toString());
            restService.put(url, null, String.class);
        }

        return RETURN_PAGE_COUPON_LIST;
    }

    /**
     * 쿠폰의 이미지를 삭제합니다.
     */
    @GetMapping("delete-image/{couponId}")
    public String deleteCouponImage(@PathVariable Long couponId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "image/", couponId.toString());
        restService.put(url, null, String.class);

        return RETURN_PAGE_COUPON_LIST;
    }

    /**
     * 쿠폰을 삭제합니다.
     * 쿠폰이 발급되어있다면, 삭제할 수 없습니다.
     */
    @GetMapping("delete/{couponId}")
    public String deleteCoupon(@PathVariable String couponId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "/", couponId);
        restService.delete(url);

        return RETURN_PAGE_COUPON_LIST;
    }
}
