package com.app.skuthon.domain.group.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor // JPA를 위한 기본 생성자
public class TeamGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 그룹명

    @Column(nullable = false)
    private String mainGoal; // 큰 목표

    @Column(nullable = false)
    private LocalDate deadline; // 마감 날짜

    // 기존 필드들 아래에 추가해 주세요.
    @Column(nullable = false, length = 10, unique = true)
    private String inviteCode;

    // 생성자 수정
    public TeamGroup(String name, String mainGoal, LocalDate deadline, String inviteCode) {
        this.name = name;
        this.mainGoal = mainGoal;
        this.deadline = deadline;
        this.inviteCode = inviteCode;
    }
}