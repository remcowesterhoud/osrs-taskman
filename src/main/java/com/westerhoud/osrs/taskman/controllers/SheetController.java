package com.westerhoud.osrs.taskman.controllers;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.westerhoud.osrs.taskman.dto.sheet.SheetDto;
import com.westerhoud.osrs.taskman.dto.sheet.SheetTaskDto;
import com.westerhoud.osrs.taskman.services.SheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/sheet")
public class SheetController {

    @Autowired
    private SheetService sheetService;
    @Value("${sheets.api.serviceaccount.email}")
    private String serviceaccountEmail;

    @PostMapping("/generate")
    SheetTaskDto generateTask(@RequestBody final SheetDto sheetDto) {
        final String spreadsheetId = sheetDto.getKey();
        try {
            return sheetService.generateTask(spreadsheetId).toDto();
        } catch (GoogleJsonResponseException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find spreadsheet with key %s. Please make sure you have set the" +
                                    " correct key in the configurations and that you have given editor access to %s.",
                            spreadsheetId, serviceaccountEmail));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong. Please try again later. If the problem persists please reach out.");
        }
    }

    @PostMapping("/complete")
    void completeTask(@RequestBody final SheetDto sheetDto) {
        final String spreadsheetId = sheetDto.getKey();
        try {
            sheetService.completeTask(spreadsheetId);
        } catch (GoogleJsonResponseException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find spreadsheet with key %s. Please make sure you have set the" +
                                    " correct key in the configurations and that you have given editor access to %s.",
                            spreadsheetId, serviceaccountEmail));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong. Please try again later. If the problem persists please reach out.");
        }
    }
}
