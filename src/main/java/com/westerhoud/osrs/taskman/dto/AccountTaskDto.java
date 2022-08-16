package com.westerhoud.osrs.taskman.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTaskDto {

    private AccountDto account;
    private TaskDto task;
}
