package com.westerhoud.osrs.taskman.controllers;

import com.westerhoud.osrs.taskman.model.Credentials;
import com.westerhoud.osrs.taskman.model.OverallProgress;
import com.westerhoud.osrs.taskman.model.Task;
import com.westerhoud.osrs.taskman.services.SheetService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/sheet")
@Slf4j
@Deprecated(forRemoval = true) // delete after version 1.1 of plugin is released
public class SheetController {

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

  @GetMapping("/current")
  Task currentTask(@RequestParam final String key) throws IOException {
    try {
      return sheetService.currentTask(new Credentials(key, "placeholder"));
    } catch (final NullPointerException e) {
      // No current task
      return TAKS_GENERATE_DTO;
    }
  }

  @PostMapping("/generate")
  Task generateTask(@RequestBody final Credentials credentials) throws IOException {
    verifyAccess(credentials.getIdentifier(), credentials.getPassword());
    return sheetService.generateTask(credentials);
  }

  @PostMapping("/complete")
  Task completeTask(@RequestBody final Credentials credentials) throws IOException {
    verifyAccess(credentials.getIdentifier(), credentials.getPassword());
    sheetService.completeTask(credentials);
    return TASK_COMPLETE_DTO;
  }

  @GetMapping("/progress")
  OverallProgress sheetProgress(@RequestParam final String key) throws IOException {
    return sheetService.progress(new Credentials(key, "placeholder"));
  }

  void verifyAccess(final String key, final String passphrase) throws IOException {
    final boolean validPassphrase = sheetService.hasCorrectPassphrase(key, passphrase);
    if (!validPassphrase) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "The passphrase configured in the plugin does not match the passhrase in the spreadsheet. "
              + "You are not allowed to modify this spreadsheet.");
    }
  }
}
