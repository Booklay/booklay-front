package com.nhnacademy.booklay.booklayfront.dto.coupon;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerStrings {

    public static final String DOMAIN_PREFIX_COUPON = "/coupon/v1";
    public static final String DOMAIN_PREFIX_SHOP = "/shop/v1";
    public static final String TARGET_VIEW = "targetUrl";
    public static final String PAGE_NUM = "pageNum";
    public static final String ERROR = "error";
    public static final String RETURN_ADMIN_PAGE = "admin/adminPage";

    public static final String ADMIN_COUPON_REST_PREFIX = "/admin/coupons/";
    public static final String ADMIN_COUPON_TYPES_REST_PREFIX = "/admin/couponTypes/";
    public static final String ADMIN_COUPON_TEMPLATE_REST_PREFIX = "/admin/couponTemplates/";
    public static final String ADMIN_COUPON_SETTING_REST_PREFIX = "/admin/couponSettings/";

    public static final String CART_REST_PREFIX = "/cart/";
    public static final String ADMIN_MEMBER_REST_PREFIX = "/admin/members/";


    public static final String COUPON_URL_LIST_PAGE = "pages";

    public static final String ATTRIBUTE_NAME_MEMBER_NO = "memberNo";
    public static final String ATTRIBUTE_NAME_COUPON_DETAIL = "couponDetail";
    public static final String ATTRIBUTE_NAME_COUPON_TEMPLATE_DETAIL = "couponTemplateDetail";
    public static final String ATTRIBUTE_NAME_COUPON_SETTING = "couponSetting";
    public static final String ATTRIBUTE_NAME_COUPON_LIST = "list";
    public static final String ATTRIBUTE_NAME_COUPON_TEMPLATE_LIST = "couponTemplateList";
    public static final String ATTRIBUTE_NAME_HISTORY_LIST = "historyList";
    public static final String ATTRIBUTE_NAME_ISSUE_LIST = "list";
    public static final String ATTRIBUTE_NAME_ISSUE_HISTORY_LIST = "list";
    public static final String ATTRIBUTE_NAME_COUPON_TYPE_LIST = "couponTypeList";
    public static final String ATTRIBUTE_NAME_COUPON_SETTING_LIST = "couponSettingList";
    public static final String ATTRIBUTE_NAME_CART_OBJECT_LIST = "cartObjectList";
}
