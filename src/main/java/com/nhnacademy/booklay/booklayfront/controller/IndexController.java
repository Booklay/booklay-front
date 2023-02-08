package com.nhnacademy.booklay.booklayfront.controller;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController {

    public IndexController(RestService restService,
                           String gatewayIp) {
    }

    @GetMapping(value = {"/index", "/"})
    public String home(MemberInfo memberInfo, Model model) {
        model.addAttribute("memberInfo", memberInfo);
        return "main/main";
    }

}
