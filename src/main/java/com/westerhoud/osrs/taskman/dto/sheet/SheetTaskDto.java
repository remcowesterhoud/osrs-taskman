package com.westerhoud.osrs.taskman.dto.sheet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SheetTaskDto {
    private String name;
    private String imageUrl;
}
