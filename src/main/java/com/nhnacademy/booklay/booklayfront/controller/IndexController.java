package com.nhnacademy.booklay.booklayfront.controller;

import com.nhnacademy.booklay.booklayfront.service.RestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController {

    public IndexController(RestService restService,
                           String gatewayIp) {
        super(restService, gatewayIp);
    }

    @GetMapping(value = {"/index", "/"})
    public String home() {
        return "index";
    }

}
