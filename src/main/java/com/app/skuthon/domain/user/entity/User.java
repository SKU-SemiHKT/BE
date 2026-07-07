package com.app.skuthon.domain.user.entity;

import com.app.skuthon.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 50, unique = true)
    private String loginId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private Integer points = 300; // 가입 시 300P

    public User(String loginId, String password, String nickname, Integer points) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.points = points;
    }
}