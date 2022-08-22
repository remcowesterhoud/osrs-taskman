package com.westerhoud.osrs.taskman.services;

import com.westerhoud.osrs.taskman.dto.AccountDto;
import com.westerhoud.osrs.taskman.dto.RegisterDto;
import com.westerhoud.osrs.taskman.model.Account;
import com.westerhoud.osrs.taskman.model.AccountTask;
import com.westerhoud.osrs.taskman.model.Role;
import com.westerhoud.osrs.taskman.model.Tier;
import com.westerhoud.osrs.taskman.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account createAccount(final RegisterDto registerDto) {
        return accountRepository.save(Account.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .tier(Tier.EASY)
                .lms(registerDto.isLms())
                .official(registerDto.isOfficial())
                .build());
    }

    @Transactional
    public AccountTask getActiveTask(final long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No account found with id '%d'", accountId)))
                .getActiveTask()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No active task found for account with id '%d'", accountId)));
    }
}
