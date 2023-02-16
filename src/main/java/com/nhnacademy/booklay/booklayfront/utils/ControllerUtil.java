package com.nhnacademy.booklay.booklayfront.utils;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.common.PageData;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchPageResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerUtil {
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    public static String buildString(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static MultiValueMap<String, String> getDefaultPageMap(PageData pageData) {
        return getDefaultPageMap(pageData.getPage(), pageData.getSize(), new MemberInfo());
    }
    public static MultiValueMap<String, String> getDefaultPageMap(PageData pageData, MemberInfo memberInfo) {
        return getDefaultPageMap(pageData.getPage(), pageData.getSize(), memberInfo);
    }

    public static MultiValueMap<String, String> getDefaultPageMap(Integer pageNum) {
        return getDefaultPageMap(pageNum, 20);
    }
    public static MultiValueMap<String, String> getDefaultPageMap(Integer pageNum, Integer size) {
        return getDefaultPageMap(pageNum, size, new MemberInfo());
    }

    public static MultiValueMap<String, String> getDefaultPageMap(Integer pageNum, MemberInfo memberInfo){
        return getDefaultPageMap(pageNum, 20, memberInfo);
    }

    public static MultiValueMap<String, String> getDefaultPageMap(Integer pageNum, Integer size, MemberInfo memberInfo){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.setAll(getMemberInfoMap(memberInfo));
        map.add("page", pageNum.toString());
        map.add("size", size.toString());
        return map;
    }

    public static Map<String, String> getMemberInfoMap(MemberInfo memberInfo){
        Map<String, String> map = new HashMap<>();
        if (memberInfo.getMemberNo() == null) {
            String nullChangeText = "\u0000";
            map.put("member_info_memberNo", nullChangeText);
            map.put("member_info_gender", nullChangeText);
            map.put("member_info_memberId", nullChangeText);
            map.put("member_info_nickname", nullChangeText);
            map.put("member_info_name", nullChangeText);
            map.put("member_info_birthday", nullChangeText);
            map.put("member_info_phoneNo", nullChangeText);
            map.put("member_info_email", nullChangeText);
        }else {
            map.put("member_info_memberNo", memberInfo.getMemberNo().toString());
            map.put("member_info_gender", memberInfo.getGender());
            map.put("member_info_memberId", memberInfo.getMemberId());
            map.put("member_info_nickname", memberInfo.getNickname());
            map.put("member_info_name", memberInfo.getName());
            map.put("member_info_birthday", memberInfo.getBirthday().toString());
            map.put("member_info_phoneNo", memberInfo.getPhoneNo());
            map.put("member_info_email", memberInfo.getEmail());
        }
        return map;
    }

    public static MultiValueMap<String, String> getMemberInfoMultiValueMap(MemberInfo memberInfo){
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.setAll(getMemberInfoMap(memberInfo));
        return multiValueMap;
    }

    public static void setCurrentPageAndMaxPageToModel(Model model, PageResponse<?> response){
        model.addAttribute("currentPage", response.getPageNumber());
        model.addAttribute("totalPage", response.getTotalPages());
        model.addAttribute("pageSize", response.getPageSize());
    }
    public static void setCurrentPageAndMaxPageToModel(Model model, SearchPageResponse<?> response){
        model.addAttribute("currentPage", response.getPageNumber());
        model.addAttribute("totalPage", response.getTotalPages());
        model.addAttribute("pageSize", response.getPageSize());
    }
}
