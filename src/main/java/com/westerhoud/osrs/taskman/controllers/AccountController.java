package com.westerhoud.osrs.taskman.controllers;

import com.westerhoud.osrs.taskman.dto.AccountDto;
import com.westerhoud.osrs.taskman.dto.AccountTaskDto;
import com.westerhoud.osrs.taskman.dto.TaskDto;
import com.westerhoud.osrs.taskman.services.AccountService;
import com.westerhoud.osrs.taskman.services.AccountTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountTaskService accountTaskService;

    @PostMapping
    AccountDto createAccount(@RequestBody final AccountDto accountDto) {
        return accountService.createAccount(accountDto).toDto();
    }

    @GetMapping("/{accountId}/task")
    AccountTaskDto getCurrentTask(@PathVariable final long accountId) {
        return accountService.getActiveTask(accountId).toDto();
    }

    @PostMapping("/{accountId}/generate")
    AccountTaskDto generateTask(@PathVariable final long accountId) {
        return accountTaskService.generateTask(accountId).toDto();
    }

    @PostMapping("/{accountId}/complete")
    AccountTaskDto completeTask(@PathVariable final long accountId) {
        return accountTaskService.completeTask(accountId).toDto();
    }
}
