package com.westerhoud.osrs.taskman.dto.site;

import com.westerhoud.osrs.taskman.model.Role;
import com.westerhoud.osrs.taskman.model.Tier;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    private long id;
    private String username;
    private Role role;
    private boolean enabled;
    private Tier tier;
    private boolean lms;
    private boolean official;
}
