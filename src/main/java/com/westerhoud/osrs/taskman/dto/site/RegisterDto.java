package com.westerhoud.osrs.taskman.dto.site;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {
    private String username;
    private String password;
    private boolean lms;
    private boolean official;
}