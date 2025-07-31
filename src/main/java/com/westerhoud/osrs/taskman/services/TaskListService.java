package com.westerhoud.osrs.taskman.services;

import com.westerhoud.osrs.taskman.model.TaskListTask;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class TaskListService {
  private final String taskListUrl;
  private final RestTemplate restTemplate;
  private Map<String, String> taskNamesById = new HashMap<>();

  public TaskListService(@Autowired final RestTemplate restTemplate, @Value("${task.list.url}") final String taskListUrl) {
    this.restTemplate = restTemplate;
    this.taskListUrl = taskListUrl;
    fetchTasks();
  }

  @Scheduled(cron = "0 0 * * * *")
  public void fetchTasks() {
    final String tracingId = String.valueOf(UUID.randomUUID());
    log.info("Fetching task list: {}", tracingId);
    ResponseEntity<Map<String, List<TaskListTask>>> response = restTemplate.exchange(
        taskListUrl,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        }
    );

    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
      log.error("Could not retrieve task list: {}!\nStatus: {}\nBody: {}", tracingId, response.getStatusCode(), response.getBody());
      return;
    }

    taskNamesById = response.getBody().values()
        .stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toMap(TaskListTask::getId, TaskListTask::getName));

    log.info("Fetched tasks successfully: {}", tracingId);
  }

  public Optional<String> getTaskName(final String taskId) {
    return Optional.ofNullable(taskNamesById.get(taskId));
  }
}
