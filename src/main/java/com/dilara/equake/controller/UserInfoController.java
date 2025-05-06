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
        // 1. Kullanıcı bilgisini veritabanına kaydet
        service.save(userInfo);

        // 2. Python ML modelinden tahmin ve tavsiye al
        String jsonAdvice = adviceService.getMLBasedAdvice(
                userInfo.getAge(),
                userInfo.getLocation(),
                userInfo.getFloorType()
        );

        // 3. Tavsiyeyi HTML sayfasına gönder
        model.addAttribute("advice", jsonAdvice);
        return "advice"; // templates/advice.html dosyasını render eder
    }

    @GetMapping("/")
    public String showUserInfoForm() {
        return "user_info"; // templates/user_info.html
    }
}
