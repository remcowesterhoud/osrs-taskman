package com.westerhoud.osrs.taskman.repositories;

import com.westerhoud.osrs.taskman.model.AccountTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTaskRepository extends JpaRepository<AccountTask, Long> {
}
