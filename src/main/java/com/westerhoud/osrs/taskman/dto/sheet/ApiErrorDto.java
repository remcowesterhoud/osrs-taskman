package com.westerhoud.osrs.taskman.dto.sheet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorDto {
    private int code;
    private String message;
}
