package com.westerhoud.osrs.taskman.model;

import com.westerhoud.osrs.taskman.dto.site.AccountDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private Tier tier;
    private boolean lms;
    private boolean official;
    @OneToMany(mappedBy = "account")
    private List<AccountTask> accountTasks;

    public List<Task> getCompletedTasks() {
        return accountTasks.stream()
                .filter(task -> task.getEndTime() != null)
                .map(AccountTask::getTask)
                .collect(Collectors.toList());
    }

    public boolean hasActiveTask() {
        return accountTasks.stream().anyMatch(task -> task.getEndTime() == null);
    }

    public Optional<AccountTask> getActiveTask() {
        return accountTasks.stream().filter(task -> task.getEndTime() == null).findFirst();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public AccountDto toDto() {
        return AccountDto.builder()
                .id(id)
                .username(username)
                .role(role)
                .enabled(enabled)
                .tier(tier)
                .build();
    }
}
