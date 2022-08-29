package com.westerhoud.osrs.taskman.controllers;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.westerhoud.osrs.taskman.dto.sheet.SheetDto;
import com.westerhoud.osrs.taskman.dto.sheet.SheetProgressDto;
import com.westerhoud.osrs.taskman.dto.sheet.SheetTaskDto;
import com.westerhoud.osrs.taskman.services.SheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/sheet")
@Slf4j
public class SheetController {

    public static final SheetTaskDto TAKS_GENERATE_DTO = SheetTaskDto.builder()
            .name("Generate a task!")
            .imageUrl("https://oldschool.runescape.wiki/images/Mystery_box.png?246bf")
            .build();
    public static final SheetTaskDto TASK_COMPLETE_DTO = SheetTaskDto.builder()
            .name("Task complete!")
            .imageUrl("https://oldschool.runescape.wiki/images/Birthday_balloons.png?356fd")
            .build();
    @Autowired
    private SheetService sheetService;

    @GetMapping("/current")
    SheetTaskDto currentTask(@RequestParam final String key, @RequestParam final String passphrase) throws IOException {
        try {
            return sheetService.currentTask(key);
        } catch (NullPointerException e) {
            // No current task
            return TAKS_GENERATE_DTO;
        }
    }

    @PostMapping("/generate")
    SheetTaskDto generateTask(@RequestBody final SheetDto sheetDto) throws IOException {
        verifyAccess(sheetDto.getKey(), sheetDto.getPassphrase());
        return sheetService.generateTask(sheetDto.getKey());
    }

    @PostMapping("/complete")
    SheetTaskDto completeTask(@RequestBody final SheetDto sheetDto) throws IOException {
        verifyAccess(sheetDto.getKey(), sheetDto.getPassphrase());
        sheetService.completeTask(sheetDto.getKey());
        return TASK_COMPLETE_DTO;
    }

    @GetMapping("/progress")
    SheetProgressDto sheetProgress(@RequestParam final String key, @RequestParam final String passphrase) throws IOException {
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
