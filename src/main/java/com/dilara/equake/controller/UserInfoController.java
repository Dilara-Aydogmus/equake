package com.dilara.equake.controller;

import com.dilara.equake.model.UserInfo;
import com.dilara.equake.service.AdviceService;
import com.dilara.equake.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private AdviceService adviceService;

    @PostMapping("/api/user-info")
    public String saveUserInfo(@ModelAttribute UserInfo userInfo, Model model) {
        service.save(userInfo);

        // Yapay zekadan kişisel öneri al
        String jsonAdvice = adviceService.getPersonalizedAdvice(
                userInfo.getAge(),
                userInfo.getLocation(),
                userInfo.getFloorType()
        );

        // Tavsiyeyi modele ekle
        model.addAttribute("advice", jsonAdvice);

        // Tavsiyeyi gösterecek sayfaya yönlendir
        return "advice"; // src/main/resources/templates/advice.html varsa
    }
}
