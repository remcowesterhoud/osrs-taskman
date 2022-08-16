package com.westerhoud.osrs.taskman.services;

import com.westerhoud.osrs.taskman.model.Account;
import com.westerhoud.osrs.taskman.model.AccountTask;
import com.westerhoud.osrs.taskman.model.Task;
import com.westerhoud.osrs.taskman.model.Tier;
import com.westerhoud.osrs.taskman.repositories.AccountRepository;
import com.westerhoud.osrs.taskman.repositories.AccountTaskRepository;
import com.westerhoud.osrs.taskman.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class AccountTaskService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AccountTaskRepository accountTaskRepository;

    @Transactional
    public AccountTask generateTask(final long accountId) {
        final var account = accountRepository.findById(accountId).orElseThrow();

        if (account.hasActiveTask()) {
            final var accountTask = account.getActiveTask().get();
            throw new IllegalStateException(String.format("You already have an active task! " +
                    "Complete '%s' before generating something new. ", accountTask.getTask().getName()));
        }

        final List<Task> uncompletedTasksForTier = getUncompletedTasksForTier(account);

        final var random = new Random();
        final var nextTask = uncompletedTasksForTier.get(random.nextInt(uncompletedTasksForTier.size()));

        final var nextAccountTask = AccountTask.builder()
                .account(account)
                .task(nextTask)
                .startTime(new Date())
                .build();
        account.getAccountTasks().add(nextAccountTask);
        accountTaskRepository.save(nextAccountTask);
        return nextAccountTask;
    }

    @Transactional
    public AccountTask completeTask(final long accountId, final long accountTaskId) {
        final var accountTask = accountTaskRepository.findById(accountTaskId).orElseThrow();
        accountTask.setEndTime(new Date());

        final var account = accountTask.getAccount();
        if (getUncompletedTasksForTier(account).isEmpty()) {
            account.setTier(Tier.nextTier(account.getTier()));
        }

        return accountTask;
    }

    private List<Task> getUncompletedTasksForTier(Account account) {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getTier().equals(account.getTier()))
                .filter(task -> !account.getCompletedTasks().contains(task))
                .toList();
    }
}
