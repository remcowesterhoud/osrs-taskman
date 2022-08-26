package com.westerhoud.osrs.taskman.controllers;

import com.westerhoud.osrs.taskman.dto.site.AccountDto;
import com.westerhoud.osrs.taskman.dto.site.LoginDto;
import com.westerhoud.osrs.taskman.model.Account;
import com.westerhoud.osrs.taskman.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping(path = "/login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> login(@RequestBody final LoginDto request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            Account account = (Account) authentication.getPrincipal();

            Instant now = Instant.now();
            long expiry = 36000L;

            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(joining(" "));

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("OSRS Taskman")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(account.getUsername())
                    .claim("roles", scope)
                    .claim("id", account.getId())
                    .build();

            String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(account.toDto());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
