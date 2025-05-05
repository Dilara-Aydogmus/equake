package com.dilara.equake.service;

import com.dilara.equake.model.UserInfo;
import com.dilara.equake.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository repo;

    public UserInfo save(UserInfo info) {
        return repo.save(info);
    }
}
