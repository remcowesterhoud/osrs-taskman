package com.westerhoud.osrs.taskman.dto;

import com.westerhoud.osrs.taskman.model.Tier;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {
    private long id;
    private String name;
    private Tier tier;
    private String info;
}
