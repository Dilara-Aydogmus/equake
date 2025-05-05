package com.dilara.equake.model;
import lombok.Data;

import jakarta.persistence.*;
@Data
@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String location;

    @Column(name = "floor_type") // veritabanı ile uyumlu
    private String floorType;

    // Getter ve Setter'lar
}
