package com.westerhoud.osrs.taskman.controllers;

import com.westerhoud.osrs.taskman.model.Credentials;
import com.westerhoud.osrs.taskman.model.OverallProgress;
import com.westerhoud.osrs.taskman.model.Task;
import com.westerhoud.osrs.taskman.model.TaskSource;
import com.westerhoud.osrs.taskman.services.SheetService;
import com.westerhoud.osrs.taskman.services.TaskService;
import com.westerhoud.osrs.taskman.services.WebsiteService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController {
  public static final String TASKMAN_IDENTIFIER_HEADER = "x-taskman-identifier";
  public static final String TASKMAN_PASSWORD_HEADER = "x-taskman-password";
  public static final String TASKMAN_SOURCE_HEADER = "x-taskman-source";
  public static final Task TAKS_GENERATE_DTO =
      Task.builder()
          .name("Generate a task!")
          .imageUrl("https://oldschool.runescape.wiki/images/Mystery_box.png?246bf")
          .build();
  public static final Task TASK_COMPLETE_DTO =
      Task.builder()
          .name("Task complete!")
          .imageUrl("https://oldschool.runescape.wiki/images/Birthday_balloons.png?356fd")
          .build();
  @Autowired private SheetService sheetService;
  @Autowired private WebsiteService websiteService;

  @GetMapping("/current")
  Task currentTask(
      @RequestHeader(TASKMAN_IDENTIFIER_HEADER) final String identifier,
      @RequestHeader(TASKMAN_PASSWORD_HEADER) final String password,
      @RequestHeader(TASKMAN_SOURCE_HEADER) final TaskSource source)
      throws IOException {
    try {
      return getService(source).currentTask(new Credentials(identifier, password));
    } catch (final NullPointerException e) {
      // No current task
      return TAKS_GENERATE_DTO;
    }
  }

  @PostMapping("/generate")
  Task generateTask(
      @RequestBody final Credentials credentials,
      @RequestHeader(TASKMAN_SOURCE_HEADER) final TaskSource source)
      throws IOException {
    return getService(source).generateTask(credentials);
  }

  @PostMapping("/complete")
  Task completeTask(
      @RequestBody final Credentials credentials,
      @RequestHeader(TASKMAN_SOURCE_HEADER) final TaskSource source)
      throws IOException {
    getService(source).completeTask(credentials);
    return TASK_COMPLETE_DTO;
  }

  @GetMapping("/progress")
  OverallProgress taskProgress(
      @RequestHeader(TASKMAN_IDENTIFIER_HEADER) final String identifier,
      @RequestHeader(TASKMAN_PASSWORD_HEADER) final String password,
      @RequestHeader(TASKMAN_SOURCE_HEADER) final TaskSource source)
      throws IOException {
    return getService(source).progress(new Credentials(identifier, password));
  }

  private TaskService getService(final TaskSource source) {
    return switch (source) {
      case SPREADSHEET -> sheetService;
      case WEBSITE -> websiteService;
    };
  }
}
