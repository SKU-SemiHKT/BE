package com.app.skuthon.domain.group.entity;

import com.app.skuthon.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 유저인지 (N:1 매핑)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 어떤 그룹인지 (N:1 매핑)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_group_id")
    private TeamGroup teamGroup;

    // 생성자
    public GroupMember(User user, TeamGroup teamGroup) {
        this.user = user;
        this.teamGroup = teamGroup;
    }
}