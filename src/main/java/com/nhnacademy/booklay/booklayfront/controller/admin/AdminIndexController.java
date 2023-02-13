package com.nhnacademy.booklay.booklayfront.controller.admin;

import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminIndexController extends BaseController {

    @GetMapping(value = {"","/", "/profile"})
    public String getAdminPage(Model model) {
        return "admin/main";
    }
}
