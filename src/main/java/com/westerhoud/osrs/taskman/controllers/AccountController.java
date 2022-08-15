package com.westerhoud.osrs.taskman.controllers;

import com.westerhoud.osrs.taskman.dto.AccountDto;
import com.westerhoud.osrs.taskman.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    AccountDto createAccount(@RequestBody final AccountDto accountDto) {
        return accountService.createAccount(accountDto).toDto();
    }
}
