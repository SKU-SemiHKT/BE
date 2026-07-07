package com.app.skuthon.domain.group.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupJoinRequest {
    private Long userId;
    private String inviteCode;
}