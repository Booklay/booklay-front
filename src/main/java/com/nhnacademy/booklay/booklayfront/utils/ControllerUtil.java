package com.nhnacademy.booklay.booklayfront.utils;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerUtil {
    public static String buildString(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static MultiValueMap<String, String> getDefaultPageMap(Integer pageNum, Integer size) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("page", pageNum.toString());
        map.add("size", size.toString());
        return map;
    }

    public static MultiValueMap<String, String> getDefaultPageMap(Integer pageNum) {
        return getDefaultPageMap(pageNum, 20);
    }

    public static void setCurrentPageAndMaxPageToModel(Model model, PageResponse<?> response){
        model.addAttribute("currentPage", response.getPageNumber());
        model.addAttribute("totalPage", response.getTotalPages());
        model.addAttribute("pageSize", response.getPageSize());
    }
}
