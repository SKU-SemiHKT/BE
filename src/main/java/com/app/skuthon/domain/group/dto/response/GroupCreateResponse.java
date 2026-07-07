package com.app.skuthon.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupCreateResponse {
    private Long groupId;
    private String name;
}