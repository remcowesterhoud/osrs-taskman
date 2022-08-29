package com.westerhoud.osrs.taskman.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SheetTask {
    private String name;
    private String imageUrl;
}
