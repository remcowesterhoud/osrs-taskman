package com.westerhoud.osrs.taskman.services;

import com.westerhoud.osrs.taskman.dto.AccountDto;
import com.westerhoud.osrs.taskman.model.Account;
import com.westerhoud.osrs.taskman.model.Role;
import com.westerhoud.osrs.taskman.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account createAccount(final AccountDto account) {
        return accountRepository.save(Account.builder()
                .username(account.getUsername())
                .password(passwordEncoder.encode(account.getPassword()))
                .role(Role.USER)
                .enabled(account.isEnabled())
                .build());
    }
}
