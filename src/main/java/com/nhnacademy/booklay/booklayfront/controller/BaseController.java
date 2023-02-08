package com.nhnacademy.booklay.booklayfront.controller;

import com.nhnacademy.booklay.booklayfront.dto.category.response.CategorySteps;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.service.category.CategoryService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class BaseController implements Controller {
    @Autowired
    private CategoryService categoryService;

    @ModelAttribute(name = "categories")
    public List<CategorySteps> categorySteps() {
        return categoryService.categorySteps();
    }

    @ModelAttribute(name = "memberInfo")
    public MemberInfo memberInfo(MemberInfo memberInfo) {
        return memberInfo;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        return null;
    }



}
