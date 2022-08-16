package com.westerhoud.osrs.taskman.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccountTaskDto {

    private AccountDto account;
    private TaskDto task;
    private Date startTime;
    private Date endTime;
}
