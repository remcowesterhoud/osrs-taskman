package com.westerhoud.osrs.taskman.repositories;

import com.westerhoud.osrs.taskman.model.Task;
import com.westerhoud.osrs.taskman.model.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
