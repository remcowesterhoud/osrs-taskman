package com.westerhoud.osrs.taskman.controllers;

import com.westerhoud.osrs.taskman.dto.AccountDto;
import com.westerhoud.osrs.taskman.dto.AccountTaskDto;
import com.westerhoud.osrs.taskman.dto.RegisterDto;
import com.westerhoud.osrs.taskman.services.AccountService;
import com.westerhoud.osrs.taskman.services.AccountTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountTaskService accountTaskService;

    @PostMapping
    AccountDto createAccount(@RequestBody final RegisterDto registerDto) {
        return accountService.createAccount(registerDto).toDto();
    }

    @GetMapping("/{accountId}/task")
    AccountTaskDto getCurrentTask(@PathVariable final long accountId) {
        checkAuthorization(accountId);
        return accountService.getActiveTask(accountId).toDto();
    }

    @PostMapping("/{accountId}/generate")
    AccountTaskDto generateTask(@PathVariable final long accountId) {
        checkAuthorization(accountId);
        return accountTaskService.generateTask(accountId).toDto();
    }

    @PostMapping("/{accountId}/complete")
    AccountTaskDto completeTask(@PathVariable final long accountId) {
        checkAuthorization(accountId);
        return accountTaskService.completeTask(accountId).toDto();
    }

    private void checkAuthorization(final long accountId) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final var jwt = (Jwt) authentication.getPrincipal();
        final var loggedInId = jwt.<Long>getClaim("id");
        if (!loggedInId.equals(accountId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have access to this resource. If you think this is a mistake please reach out.");
        }
    }
}
