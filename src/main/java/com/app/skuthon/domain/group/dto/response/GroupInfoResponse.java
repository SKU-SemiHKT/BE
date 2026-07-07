package com.app.skuthon.domain.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GroupInfoResponse {
    private Long groupId;
    private String name;
    private String mainGoal;
    private LocalDate deadline;
}