package com.dilara.equake.repository;

import com.dilara.equake.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findTopByOrderByIdDesc(); // En son girilen kullanıcıyı getirir
}
