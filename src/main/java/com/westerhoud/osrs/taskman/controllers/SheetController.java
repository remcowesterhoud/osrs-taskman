package com.westerhoud.osrs.taskman.controllers;

import com.westerhoud.osrs.taskman.model.Sheet;
import com.westerhoud.osrs.taskman.model.SheetProgress;
import com.westerhoud.osrs.taskman.model.SheetTask;
import com.westerhoud.osrs.taskman.services.SheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/sheet")
@Slf4j
public class SheetController {

    public static final SheetTask TAKS_GENERATE_DTO = SheetTask.builder()
            .name("Generate a task!")
            .imageUrl("https://oldschool.runescape.wiki/images/Mystery_box.png?246bf")
            .build();
    public static final SheetTask TASK_COMPLETE_DTO = SheetTask.builder()
            .name("Task complete!")
            .imageUrl("https://oldschool.runescape.wiki/images/Birthday_balloons.png?356fd")
            .build();
    @Autowired
    private SheetService sheetService;

    @GetMapping("/current")
    SheetTask currentTask(@RequestParam final String key) throws IOException {
        try {
            return sheetService.currentTask(key);
        } catch (NullPointerException e) {
            // No current task
            return TAKS_GENERATE_DTO;
        }
    }

    @PostMapping("/generate")
    SheetTask generateTask(@RequestBody final Sheet sheet) throws IOException {
        verifyAccess(sheet.getKey(), sheet.getPassphrase());
        return sheetService.generateTask(sheet.getKey());
    }

    @PostMapping("/complete")
    SheetTask completeTask(@RequestBody final Sheet sheet) throws IOException {
        verifyAccess(sheet.getKey(), sheet.getPassphrase());
        sheetService.completeTask(sheet.getKey());
        return TASK_COMPLETE_DTO;
    }

    @GetMapping("/progress")
    SheetProgress sheetProgress(@RequestParam final String key) throws IOException {
        return sheetService.progress(key);
    }

    void verifyAccess(final String key, final String passphrase) throws IOException {
        boolean validPassphrase = sheetService.hasCorrectPassphrase(key, passphrase);
        if (!validPassphrase) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "The passphrase configured in the plugin does not match the passhrase in the spreadsheet. " +
                            "You are not allowed to modify this spreadsheet.");
        }
    }
}
