package com.dilara.equake.controller;

import com.dilara.equake.model.UserInfo;
import com.dilara.equake.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService service;

    @PostMapping("/api/user-info")
    public String saveUserInfo(@ModelAttribute UserInfo userInfo) {
        service.save(userInfo);
        return "redirect:/analyze.html"; // veya başka bir sayfaya yönlendirme
    }
}
