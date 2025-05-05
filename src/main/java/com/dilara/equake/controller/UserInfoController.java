package com.dilara.equake.controller;

import com.dilara.equake.model.UserInfo;
import com.dilara.equake.service.AdviceService;
import com.dilara.equake.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private AdviceService adviceService;

    @PostMapping("/api/user-info")
    public String saveUserInfo(@ModelAttribute UserInfo userInfo, Model model) {
        service.save(userInfo);

        String jsonAdvice = adviceService.getPersonalizedAdvice(
                userInfo.getAge(),
                userInfo.getLocation(),
                userInfo.getFloorType()
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonAdvice);
            String plainAdvice = node.get("advice").asText();
            model.addAttribute("advice", plainAdvice);
        } catch (Exception e) {
            model.addAttribute("advice", "Tavsiye alınamadı: " + e.getMessage());
        }

        return "advice";
    }


    @GetMapping("/")
    public String showUserInfoForm() {
        return "user_info"; // templates/user_info.html dosyasını göster
    }

}
