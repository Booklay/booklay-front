package com.nhnacademy.booklay.booklayfront.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

//@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Component
@SuppressWarnings("unchecked")
public class ControllerUtil {
    private static ObjectMapper objectMapper;

    @Autowired
    private ControllerUtil(ObjectMapper objectMapper) {
        ControllerUtil.objectMapper = objectMapper;
    }

    public static String buildString(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static MultiValueMap<String, String> getDefaultPageMap(Integer pageNum, Integer size) {
        return getDefaultPageMap(pageNum, size, new MemberInfo());
    }

    public static MultiValueMap<String, String> getDefaultPageMap(Integer pageNum) {
        return getDefaultPageMap(pageNum, 20);
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
        if (memberInfo.getMemberId() == null) {
            Map<String, String> map = new HashMap<>();
            map.put("memberNo", null);
            map.put("gender", null);
            map.put("memberId", null);
            map.put("nickname", null);
            map.put("name", null);
            map.put("birthday", null);
            map.put("phoneNo", null);
            map.put("email", null);
            return map;
        }else {
            return objectMapper.convertValue(memberInfo, MultiValueMap.class);
        }
    }

    public static void setCurrentPageAndMaxPageToModel(Model model, PageResponse<?> response){
        model.addAttribute("currentPage", response.getPageNumber());
        model.addAttribute("totalPage", response.getTotalPages());
        model.addAttribute("pageSize", response.getPageSize());
    }
}
