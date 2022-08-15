package com.westerhoud.osrs.taskman.dto;

import com.westerhoud.osrs.taskman.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    private String username;
    private String password;
    private Role role;
    private boolean enabled;
}
