package com.westerhoud.osrs.taskman.controllers;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.westerhoud.osrs.taskman.dto.sheet.SheetDto;
import com.westerhoud.osrs.taskman.dto.sheet.SheetTaskDto;
import com.westerhoud.osrs.taskman.services.SheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/sheet")
public class SheetController {

    @Autowired
    private SheetService sheetService;
    @Value("${sheets.api.serviceaccount.email}")
    private String serviceaccountEmail;

    @GetMapping("/current")
    SheetTaskDto currentTask(@RequestParam final String key, @RequestParam final String passphrase) {
        verifyAccess(key, passphrase);
        try {
            return sheetService.currentTask(key);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong. Please try again later. If the problem persists please reach out.");
        }
    }

    @PostMapping("/generate")
    SheetTaskDto generateTask(@RequestBody final SheetDto sheetDto) {
        verifyAccess(sheetDto.getKey(), sheetDto.getPassphrase());
        try {
            return sheetService.generateTask(sheetDto.getKey());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong. Please try again later. If the problem persists please reach out.");
        }
    }

    @PostMapping("/complete")
    void completeTask(@RequestBody final SheetDto sheetDto) {
        verifyAccess(sheetDto.getKey(), sheetDto.getPassphrase());
        try {
            sheetService.completeTask(sheetDto.getKey());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong. Please try again later. If the problem persists please reach out.");
        }
    }

    void verifyAccess(final String key, final String passphrase) {
        try {
            boolean validPassphrase = sheetService.hasCorrectPassphrase(key, passphrase);
            if (!validPassphrase) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "The passphrase configured in the plugin does not match the passhrase in the spreadsheet. " +
                                "You are not allowed to modify this spreadsheet.");
            }
        } catch (GoogleJsonResponseException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find spreadsheet with key %s. Please make sure you have set the" +
                                    " correct key in the configurations and that you have given editor access to %s.",
                            key, serviceaccountEmail));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong. Please try again later. If the problem persists please reach out.");
        }
    }
}
