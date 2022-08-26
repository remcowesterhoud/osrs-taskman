package com.westerhoud.osrs.taskman.controllers;

import com.westerhoud.osrs.taskman.dto.sheet.SheetDto;
import com.westerhoud.osrs.taskman.dto.sheet.SheetTaskDto;
import com.westerhoud.osrs.taskman.services.SheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/sheet")
public class SheetController {

    @Autowired
    private SheetService sheetService;

    @PostMapping("/generate")
    SheetTaskDto generateTask(@RequestBody final SheetDto sheetDto) throws IOException {
        return sheetService.generateTask(sheetDto.getKey()).toDto();
    }

    @PostMapping("/complete")
    void completeTask(@RequestBody final SheetDto sheetDto) {

    }
}
