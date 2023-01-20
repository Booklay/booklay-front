package com.nhnacademy.booklay.booklayfront.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminIndexController {

    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("navHead", "emptyPage");
        model.addAttribute("targetUrl", "emptyPage");
        return "/admin/adminPage";
    }
}
