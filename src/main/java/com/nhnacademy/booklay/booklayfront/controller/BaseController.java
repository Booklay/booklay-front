package com.nhnacademy.booklay.booklayfront.controller;

import com.nhnacademy.booklay.booklayfront.dto.category.response.CategorySteps;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


public class BaseController implements Controller {

    private final RestService restService;
    private final String redirectGatewayPrefix;

    public BaseController(RestService restService, String gatewayIp) {
        this.restService = restService;
        this.redirectGatewayPrefix = gatewayIp + "/shop/v1" + "/admin/categories";
    }

    @ModelAttribute(name = "steps")
    public CategorySteps categorySteps() {
        ApiEntity<CategorySteps> categoryStepsApiEntity =
            restService.get(redirectGatewayPrefix + "/steps/1", null,
                CategorySteps.class);

        return categoryStepsApiEntity.getBody();
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        return null;
    }

}
