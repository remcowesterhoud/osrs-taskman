package com.westerhoud.osrs.taskman.controllers;

import com.westerhoud.osrs.taskman.dto.AccountTaskDto;
import com.westerhoud.osrs.taskman.model.AccountTask;
import com.westerhoud.osrs.taskman.services.AccountTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/account/{accountId}/task")
public class AccountTaskController {

    @Autowired
    private AccountTaskService accountTaskService;

    @PostMapping("/generate")
    AccountTaskDto generateTask(@PathVariable final long accountId) {
        return accountTaskService.generateTask(accountId).toDto();
    }
}
